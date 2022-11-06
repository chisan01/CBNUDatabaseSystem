import java.sql.DriverManager

fun main(args: Array<String>) {
    getAllBook().forEach(::println)
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
        val rs = stmt.executeQuery("SELECT * FROM Book")
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