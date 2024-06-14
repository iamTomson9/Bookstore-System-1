import javafx.collections.ObservableList;
import java.util.List;
import java.util.ArrayList;

public class Inventory {
    private ObservableList<Book> bookList;

    public Inventory() {
        this.bookList = javafx.collections.FXCollections.observableArrayList();
    }

    public ObservableList<Book> getBookList() {
        return this.bookList;
    }

    public void addBook(Book book) {
        bookList.add(book);
    }

    public List<Book> searchBook(String criteria, String query) {
        List<Book> results = new ArrayList<>();
        for (Book book : bookList) {
            switch (criteria) {
                case "Title":
                    if (book.titleProperty().get().toLowerCase().contains(query.toLowerCase())) {
                        results.add(book);
                    }
                    break;
                case "Author":
                    if (book.authorProperty().get().toLowerCase().contains(query.toLowerCase())) {
                        results.add(book);
                    }
                    break;
                case "Genre":
                    if (book.genreProperty().get().toLowerCase().contains(query.toLowerCase())) {
                        results.add(book);
                    }
                    break;
                default:
                    break;
            }
        }
        return results;
    }
}