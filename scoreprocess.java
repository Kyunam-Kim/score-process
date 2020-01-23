import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ScoreProcessProgram {
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		String dburl = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			dburl = "jdbc:oracle:thin:@localhost:1521:xe";
			con = DriverManager.getConnection(dburl, "scoredata", "score");

			Scanner in = new Scanner(System.in);
			int menu;
			boolean sw = true;

			while (sw) {
				System.out.println("============");
				System.out.println("| 1. 성적 입력 |");
				System.out.println("| 2. 성적 수정 |");
				System.out.println("| 3. 이름 수정 |");
				System.out.println("| 4. 성적 삭제 |");
				System.out.println("| 5. 성적 검색 |");
				System.out.println("| 6. 성적 출력 |");
				System.out.println("| 7. 종료        |");
				System.out.println("============");
				System.out.print("메뉴를 선택하세요: ");

				menu = Integer.parseInt(in.nextLine());

				switch (menu) {
				case 1:
					String name;
					int student_number, language_score, english_score, math_score;
					System.out.println("학번을 입력하세요");
					student_number = Integer.parseInt(in.nextLine());

					System.out.println("이름을 입력하세요");
					name = in.nextLine();

					System.out.println("국어 성적을 입력하세요.");
					language_score = Integer.parseInt(in.nextLine());

					System.out.println("영어 성적을 입력하세요");
					english_score = Integer.parseInt(in.nextLine());

					System.out.println("수학 성적을 입력하세요");
					math_score = Integer.parseInt(in.nextLine());

					pstmt = con.prepareStatement("INSERT INTO score VALUES(?,?,?,?,?)");
					pstmt.setInt(1, student_number);
					pstmt.setString(2, name);
					pstmt.setInt(3, language_score);
					pstmt.setInt(4, english_score);
					pstmt.setInt(5, math_score);
					pstmt.executeUpdate();
					break;

				case 2:
					System.out.println("성적 수정할 학번을 입력하세요");
					student_number = Integer.parseInt(in.nextLine());

					System.out.println("국어 성적을 수정하세요.");
					language_score = Integer.parseInt(in.nextLine());

					System.out.println("영어 성적을 수정하세요");
					english_score = Integer.parseInt(in.nextLine());

					System.out.println("수학 성적을 수정하세요");
					math_score = Integer.parseInt(in.nextLine());

					pstmt = con.prepareStatement(
							"UPDATE score SET language_score = ?, english_score = ?, math_score = ? WHERE student_number = ?");

					pstmt.setInt(1, language_score);
					pstmt.setInt(2, english_score);
					pstmt.setInt(3, math_score);
					pstmt.setInt(4, student_number);
					pstmt.executeUpdate();
					break;

				case 3:
					String rename;

					System.out.println("이름 수정할 학번을 입력하세요");
					student_number = Integer.parseInt(in.nextLine());

					System.out.println("새로운 이름을 입력하세요");
					rename = in.nextLine();

					pstmt = con.prepareStatement("UPDATE score SET name = ? WHERE student_number = ?");
					pstmt.setString(1, rename);
					pstmt.setInt(2, student_number);
					pstmt.executeUpdate();
					break;
				case 4:
					System.out.println("삭제할 학번을 입력하세요");
					student_number = Integer.parseInt(in.nextLine());

					pstmt = con.prepareStatement("DELETE FROM score WHERE student_number = ?");
					pstmt.setInt(1, student_number);
					pstmt.executeUpdate();
					break;
				case 5:
					int search_student_number;
					System.out.println("조회할 학생의 학번을 입력하세요");
					search_student_number = Integer.parseInt(in.nextLine());

					pstmt = con.prepareStatement("SELECT * FROM score WHERE student_number = ?"); // 테이블 이름 넣을것
					pstmt.setInt(1, search_student_number);
					rs = pstmt.executeQuery();

					System.out.println("학번 \t 이름 \t 국어 성적 \t 영어 성적 \t 수학 성적");

					if (rs.next()) {
						System.out.print(rs.getString("student_number") + "\t");
						System.out.print(rs.getString("name") + "\t");
						System.out.print(rs.getString("language_score") + "\t");
						System.out.print(rs.getString("english_score") + "\t");
						System.out.print(rs.getString("math_score"));
						System.out.println();
					}
					break;

				case 6:
					pstmt = con.prepareStatement(
							"SELECT student_number, name, language_score, english_score , math_score, total, avg, rank FROM scoreView");
					rs = pstmt.executeQuery();
					System.out.println("학번 \t 이름 \t 국어 성적 \t 영어 성적 \t 수학 성적 \t 총점 \t 평균 \t 순위");
					while (rs.next()) {
						System.out.print(rs.getString("student_number") + "\t");
						System.out.print(rs.getString("name") + "\t");
						System.out.print(rs.getString("language_score") + "\t");
						System.out.print(rs.getString("english_score") + "\t");
						System.out.print(rs.getString("math_score") + "\t");
						System.out.print(rs.getString("total") + "\t");
						System.out.print(rs.getDouble("avg") + "\t");
						System.out.print(rs.getString("rank"));
						System.out.println();
					}
					pstmt.close();
					rs.close();
					System.out.println("국어 평균점수 \t 영어 평균점수 \t 수학 평균점수");
					pstmt = con.prepareStatement(
							"SELECT AVG(language_score), AVG(english_score), AVG(math_score) FROM score");
					rs = pstmt.executeQuery();
					while (rs.next()) {
						System.out.print(rs.getDouble("AVG(language_score)") + "\t");
						System.out.print(rs.getDouble("AVG(english_score)") + "\t");
						System.out.print(rs.getDouble("AVG(math_score)"));
						System.out.println();
					}		
					break;
				case 7:
					System.out.println("종료되었습니다.");
					sw = false;
					break;
				}
			}
		} catch (IllegalArgumentException e) {
			System.out.println("입력 형태를 확인하세요");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
		}
	}
}
