package calorieTracker;

import custom.Custom;
import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;

import pl.coderion.model.Nutriments;
import pl.coderion.model.Product;
import pl.coderion.model.ProductResponse;
import pl.coderion.service.OpenFoodFactsWrapper;
import pl.coderion.service.impl.OpenFoodFactsWrapperImpl;

public class CalorieTracker {
    private JFrame frame;
    private JTextField txtSearch;
    private JTextField txtWeight;
    private JLabel lblResult;
    private JLabel lblProductImage;
    private JButton btnSearch;
    private JButton btnCalculate;
    private JButton btnBack;

    private int selectedFoodCaloriesPer100g = 0;
    private String selectedFoodName = "";
    private OpenFoodFactsWrapper openFoodFactsWrapper;

    public CalorieTracker() {
        this.frame = new JFrame("Calorie Tracker");

        this.openFoodFactsWrapper = new OpenFoodFactsWrapperImpl();
    }

    public void showTracker() {
        this.frame.setSize(500, 700);
        this.frame.setLayout(new BorderLayout(15, 15));
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Custom.background(frame);

        JPanel pnlSearch = new JPanel(new GridLayout(2, 1, 5, 5));
        pnlSearch.setOpaque(false);

        JLabel lblSearch = new JLabel("Enter Barcode:", SwingConstants.CENTER);
        lblSearch.setFont(new Font("SansSerif", Font.BOLD, 14));

        JPanel pnlSearchInput = new JPanel(new BorderLayout(5, 5));
        pnlSearchInput.setOpaque(false);
        txtSearch = new JTextField();
        txtSearch.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btnSearch = new JButton("FETCH API");
        Custom.startButton(btnSearch);

        pnlSearchInput.add(txtSearch, BorderLayout.CENTER);
        pnlSearchInput.add(btnSearch, BorderLayout.EAST);

        pnlSearch.add(lblSearch);
        pnlSearch.add(pnlSearchInput);
        this.frame.add(pnlSearch, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.setOpaque(false);

        lblResult = new JLabel("Scan barcode and enter weight", SwingConstants.CENTER);
        lblResult.setFont(new Font("Monospaced", Font.BOLD, 18));
        pnlCenter.add(lblResult, BorderLayout.NORTH);

        lblProductImage = new JLabel("", SwingConstants.CENTER);
        pnlCenter.add(lblProductImage, BorderLayout.CENTER);


        JPanel pnlCalcInputs = new JPanel(new GridLayout(3, 1, 5, 5));
        pnlCalcInputs.setOpaque(false);

        JLabel lblWeight = new JLabel("Enter weight eaten (grams):", SwingConstants.CENTER);
        lblWeight.setFont(new Font("SansSerif", Font.BOLD, 14));
        txtWeight = new JTextField();
        txtWeight.setFont(new Font("SansSerif", Font.PLAIN, 20));
        txtWeight.setHorizontalAlignment(JTextField.CENTER);
        btnCalculate = new JButton("CALCULATE CALORIES");
        Custom.startButton(btnCalculate);

        pnlCalcInputs.add(lblWeight);
        pnlCalcInputs.add(txtWeight);
        pnlCalcInputs.add(btnCalculate);
        pnlCenter.add(pnlCalcInputs, BorderLayout.SOUTH);

        this.frame.add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlControls = new JPanel(new GridLayout(1, 1, 5, 5));
        pnlControls.setOpaque(false);
        btnBack = new JButton("BACK");
        Custom.startButton(btnBack);

        pnlControls.add(btnBack);
        this.frame.add(pnlControls, BorderLayout.SOUTH);

        btnSearch.addActionListener(e -> {
            String barcode = txtSearch.getText().trim();
            if (barcode.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a barcode!", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            lblResult.setText("Fetching from API...");
            lblProductImage.setIcon(null);
            lblProductImage.setText("");

            new Thread(() -> {
                try {

                    ProductResponse productResponse = openFoodFactsWrapper.fetchProductByCode(barcode);

                    if (!productResponse.isStatus()) {
                        SwingUtilities.invokeLater(() -> {
                            lblResult.setText("Product not found in database!");
                            lblResult.setForeground(Color.RED);
                            selectedFoodCaloriesPer100g = 0;
                        });
                        return;
                    }

                    Product product = productResponse.getProduct();
                    selectedFoodName = product.getProductName();
                    if (selectedFoodName == null || selectedFoodName.isEmpty()) {
                        selectedFoodName = "Unknown Product";
                    }

                    Nutriments nutriments = product.getNutriments();
                    if (nutriments != null) {
                        selectedFoodCaloriesPer100g = nutriments.getEnergyKcal();
                    } else {
                        selectedFoodCaloriesPer100g = 0;
                    }


                    String imageUrl = "";
                    if (product.getSelectedImages() != null &&
                            product.getSelectedImages().getFront() != null &&
                            product.getSelectedImages().getFront().getDisplay() != null) {
                        imageUrl = product.getSelectedImages().getFront().getDisplay().getUrl();
                    }

                    ImageIcon finalIcon = null;
                    if (imageUrl != null && !imageUrl.isEmpty()) {

                        URL imgUrl = URI.create(imageUrl).toURL();
                        Image img = Toolkit.getDefaultToolkit().createImage(imgUrl);
                        ImageIcon rawIcon = new ImageIcon(img);
                        Image scaledImg = rawIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                        finalIcon = new ImageIcon(scaledImg);
                    }

                    final ImageIcon displayIcon = finalIcon;
                    SwingUtilities.invokeLater(() -> {
                        lblResult.setText("<html><center>" + selectedFoodName + "<br>(" + selectedFoodCaloriesPer100g + " kcal/100g)</center></html>");
                        lblResult.setForeground(Color.BLACK);
                        if (displayIcon != null) {
                            lblProductImage.setIcon(displayIcon);
                        } else {
                            lblProductImage.setText("No image available");
                        }
                    });

                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        lblResult.setText("API Error or No Connection!");
                        lblResult.setForeground(Color.RED);
                        selectedFoodCaloriesPer100g = 0;
                    });
                    ex.printStackTrace();
                }
            }).start();
        });

        btnCalculate.addActionListener(e -> {
            if (selectedFoodCaloriesPer100g == 0) {
                JOptionPane.showMessageDialog(frame, "Please fetch a valid product from API first!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double weight = Double.parseDouble(txtWeight.getText().trim());
                if (weight <= 0) throw new NumberFormatException();

                double totalCalories = (selectedFoodCaloriesPer100g / 100.0) * weight;
                lblResult.setText(String.format("Total: %.1f kcal", totalCalories));
                lblResult.setForeground(new Color(0, 128, 0));

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid weight in grams!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });


        btnBack.addActionListener(e -> {
            this.frame.dispose();
            new app.App().showApp();
        });

        this.frame.setVisible(true);
    }
}

