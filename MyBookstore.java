import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MyBookstore extends Application {
    private Inventory myInventory;
    private Sales mySales;
    private CustomerManagement myCustomerManagement;

    private TableView<Book> table;
    private TextField isbnField, titleField, authorField, genreField, priceField, quantityField;
    private TextField searchField;
    private ComboBox<String> searchCriteriaComboBox;
    private TextArea resultTextArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        myInventory = new Inventory();
        mySales = new Sales(myInventory);
        myCustomerManagement = new CustomerManagement();

        primaryStage.setTitle("MyBookstore");

      
        VBox layout = new VBox();
        layout.setSpacing(10);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: yellowgreen; -fx-text-fill: #FFFFFF;");

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(e -> primaryStage.close());
        fileMenu.getItems().add(exitMenuItem);
        menuBar.getMenus().add(fileMenu);

        GridPane addBookPane = new GridPane();
        addBookPane.setPadding(new Insets(10));
        addBookPane.setVgap(5);
        addBookPane.setHgap(5);

        isbnField = new TextField("978-3-16-148410-0");
        titleField = new TextField("Java Programming");
        authorField = new TextField("John Doe");
        genreField = new TextField("Programming");
        priceField = new TextField("29.99");
        quantityField = new TextField("50");
        Button addButton = new Button("Add Book");
        addButton.setOnAction(e -> addBook());

        addBookPane.add(new Label("ISBN:"), 0, 0);
        addBookPane.add(isbnField, 1, 0);
        addBookPane.add(new Label("Title:"), 0, 1);
        addBookPane.add(titleField, 1, 1);
        addBookPane.add(new Label("Author:"), 0, 2);
        addBookPane.add(authorField, 1, 2);
        addBookPane.add(new Label("Genre:"), 0, 3);
        addBookPane.add(genreField, 1, 3);
        addBookPane.add(new Label("Price:"), 0, 4);
        addBookPane.add(priceField, 1, 4);
        addBookPane.add(new Label("Quantity:"), 0, 5);
        addBookPane.add(quantityField, 1, 5);
        addBookPane.add(addButton, 1, 6);

        VBox manageInventoryPane = new VBox(10);
        manageInventoryPane.setPadding(new Insets(10));
        table = new TableView<>();
        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        TableColumn<Book, String> genreColumn = new TableColumn<>("Genre");
        TableColumn<Book, Double> priceColumn = new TableColumn<>("Price");
        TableColumn<Book, Integer> quantityColumn = new TableColumn<>("Quantity");
        table.getColumns().addAll(isbnColumn, titleColumn, authorColumn, genreColumn, priceColumn, quantityColumn);
        table.setItems(myInventory.getBookList());

        HBox searchBox = new HBox(10);
        searchField = new TextField();
        searchField.setPromptText("Search...");
        searchCriteriaComboBox = new ComboBox<>();
        searchCriteriaComboBox.getItems().addAll("Title", "Author", "Genre");
        searchCriteriaComboBox.setValue("Title");
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchBook());
        searchBox.getChildren().addAll(searchField, searchCriteriaComboBox, searchButton);

        resultTextArea = new TextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setPrefHeight(200);

        manageInventoryPane.getChildren().addAll(table, searchBox, resultTextArea);

        VBox revenuePane = new VBox(10);
        revenuePane.setPadding(new Insets(10));
        Button calculateRevenueButton = new Button("Calculate Revenue");
        calculateRevenueButton.setOnAction(e -> calculateRevenue());
        revenuePane.getChildren().addAll(calculateRevenueButton);

        layout.getChildren().addAll(menuBar, addBookPane, manageInventoryPane, revenuePane);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void viewBooks() {
        TableView<Book> bookTableView = new TableView<>();
        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(data -> data.getValue().isbnProperty());
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(data -> data.getValue().titleProperty());
        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(data -> data.getValue().authorProperty());
        TableColumn<Book, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(data -> data.getValue().genreProperty());
        TableColumn<Book, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(data -> data.getValue().priceProperty().asObject());
        TableColumn<Book, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());

        bookTableView.getColumns().addAll(isbnColumn, titleColumn, authorColumn, genreColumn, priceColumn, quantityColumn);
        bookTableView.setItems(myInventory.getBookList());

        // Create a new stage for viewing books
        Stage viewBooksStage = new Stage();
        viewBooksStage.setTitle("View Books");
        viewBooksStage.setScene(new Scene(new VBox(new Label("Books in Inventory"), bookTableView), 600, 400));
        viewBooksStage.show();
    }
     private void addBook() {
        String isbn = isbnField.getText();
        String title = titleField.getText();
        String author = authorField.getText();
        String genre = genreField.getText();
        double price = Double.parseDouble(priceField.getText());
        int quantity = Integer.parseInt(quantityField.getText());
        Book book = new Book(isbn, title, author, genre, price, quantity);
        myInventory.addBook(book);
        table.setItems(myInventory.getBookList());
        clearFields();
    }

    private void searchBook() {
        String query = searchField.getText();
        String criteria = searchCriteriaComboBox.getValue();
        List<Book> results = myInventory.searchBook(criteria, query);
        resultTextArea.clear();
        for (Book book : results) {
            resultTextArea.appendText(book.toString() + "\n");
        }
    }

    private void calculateRevenue() {
        double revenue = mySales.calculateRevenue();
        resultTextArea.clear();
        resultTextArea.setText("Total Revenue: $" + revenue);
    }

    private void clearFields() {
        isbnField.clear();
        titleField.clear();
        authorField.clear();
        genreField.clear();
        priceField.clear();
        quantityField.clear();
    }
}