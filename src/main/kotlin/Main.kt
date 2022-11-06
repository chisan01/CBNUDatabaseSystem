import java.sql.DriverManager

fun main(args: Array<String>) {
//    val allBook = getAllBook()
//    allBook.forEach(::println)
//
//    addBook(Book(
//        bookId = allBook.last().bookId + 1,
//        bookName = "test",
//        publisher = "chisan",
//        price = 10000
//    ))
//
//    println("-------- delete last Book ------------")
//    deleteBook(allBook.last().bookId)

    getAllBook().forEach(::println)

    println("--------- '축구의 역사' 검색 ----------")
    val searchResult = searchBookByBookName("축구의 역사")
    if(searchResult == null) println("존재하지 않는 도서입니다")
    else println(searchResult)

    println("--------- '축구의 역사2' 검색 ----------")
    val searchResult2 = searchBookByBookName("축구의 역사2")
    if(searchResult2 == null) println("존재하지 않는 도서입니다")
    else println(searchResult2)
}

fun getAllBook(): List<Book> {
    val result = mutableListOf<Book>()
    try {
        Class.forName("com.mysql.cj.jdbc.Driver")
        val con = DriverManager.getConnection(
            "jdbc:mysql://192.168.55.189:4567/madang",
            "chisanahn", "1234"
        )
        val stmt = con.createStatement()
        val rs = stmt.executeQuery("SELECT * FROM Book ORDER BY bookid")
        while (rs.next()) {
            result.add(Book(
                bookId = rs.getInt(1),
                bookName = rs.getString(2),
                publisher = rs.getString(3),
                price = rs.getInt(4)
            ))
        }
        con.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return result
}

fun addBook(book: Book) {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver")
        val con = DriverManager.getConnection(
            "jdbc:mysql://192.168.55.189:4567/madang",
            "chisanahn", "1234"
        )
        val pstmt = con.prepareStatement("INSERT INTO Book(bookid, bookname, publisher, price) values(?, ?, ?, ?)")
        pstmt.setInt(1, book.bookId)
        pstmt.setString(2, book.bookName)
        pstmt.setString(3, book.publisher)
        book.price?.let { pstmt.setInt(4, it) }

        pstmt.executeUpdate()
        con.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
fun deleteBook(bookId: Int) {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver")
        val con = DriverManager.getConnection(
            "jdbc:mysql://192.168.55.189:4567/madang",
            "chisanahn", "1234"
        )
        val pstmt = con.prepareStatement("DELETE FROM Book where bookid=?")
        pstmt.setInt(1, bookId)
        pstmt.executeUpdate()
        con.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun searchBookByBookName(bookName: String): Book? {
    var searchResult: Book? = null
    try {
        Class.forName("com.mysql.cj.jdbc.Driver")
        val con = DriverManager.getConnection(
            "jdbc:mysql://192.168.55.189:4567/madang",
            "chisanahn", "1234"
        )
        val pstmt = con.prepareStatement("SELECT * FROM Book WHERE bookname=?")
        pstmt.setString(1, bookName)
        val rs = pstmt.executeQuery()
        if(rs.next()) searchResult = Book(
            bookId = rs.getInt(1),
            bookName = rs.getString(2),
            publisher = rs.getString(3),
            price = rs.getInt(4)
        )
        con.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return searchResult
}