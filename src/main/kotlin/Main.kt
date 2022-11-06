import java.sql.DriverManager

fun main(args: Array<String>) {
    println("---------- 책 관리 프로그램 -----------")
    while (true) {
        println("1) 모든 책 목록 보기")
        println("2) 제목으로 책 검색하기")
        println("3) 책 추가하기")
        println("4) 책 삭제하기")
        println("5) 프로그램 종료")
        print("(1) ~ (5) 중 원하는 메뉴를 선택하세요: ")
        val menu = readLine()?.toInt()
        when (menu) {
            1 -> {
                getAllBook().forEach(::println)
            }
            2 -> {
                print("검색하려는 책 제목을 입력하세요: ")
                val bookName = readLine() ?: ""
                val searchResult = searchBookByBookName(bookName)
                if (searchResult == null) println("존재하지 않는 도서입니다")
                else println(searchResult)
            }
            3 -> {
                println("추가하려는 책의 정보를 입력해주세요")
                print("bookId: ")
                val bookId = readLine()?.toInt() ?: -1
                print("bookName: ")
                val bookName = readLine()
                print("publisher: ")
                val publisher = readLine()
                print("price: ")
                val price = readLine()?.toInt()
                addBook(Book(bookId, bookName, publisher, price))
            }
            4 -> {
                print("삭제하려는 책의 bookId를 입력하세요: ")
                val bookId = readLine()?.toInt() ?: -1
                deleteBook(bookId)
            }
            5 -> return
            else -> {
                println("올바른 메뉴를 입력해주세요")
            }
        }
        println()
    }
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
            result.add(
                Book(
                    bookId = rs.getInt(1),
                    bookName = rs.getString(2),
                    publisher = rs.getString(3),
                    price = rs.getInt(4)
                )
            )
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
        if (rs.next()) searchResult = Book(
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