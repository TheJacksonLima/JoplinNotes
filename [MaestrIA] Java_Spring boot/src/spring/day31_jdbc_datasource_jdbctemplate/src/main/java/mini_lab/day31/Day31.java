package mini_lab.day31;

import java.sql.*;

public class Day31 {

    public static void main(String[] args) throws Exception {
        try (
                Connection c = DriverManager.getConnection("jdbc:h2:mem:test");
                Statement s = c.createStatement()
        ) {

            s.execute(
                    """
                    create table property(
                        id int primary key,
                        title varchar(100)
                    )
                    """
            );

            s.execute(
                    """
                    insert into property
                    values(1, 'Batel')
                    """
            );

            try (
                    ResultSet rs = s.executeQuery("select * from property")
                    //ResultSet rs = s.executeQuery("select * from propert")
            ) {

                while (rs.next()) {
                    Property property = new Property(rs.getInt("id"), rs.getString("title"));
                    System.out.println(property);
                }
            }
        }
    }
}