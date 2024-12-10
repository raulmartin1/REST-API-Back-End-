<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import = "java.sql.*" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Database SQL Load</title>
    </head>
    <style>
        .error {
            color: red;
        }
        pre {
            color: green;
        }
    </style>
    <body>
        <h2>Database SQL Load</h2>
        <%
            /* How to customize:
             * 1. Update the database name on dbname.
             * 2. Create the list of tables, under tablenames[].
             * 3. Create the list of table definition, under tables[].
             * 4. Create the data into the above table, under data[]. 
             * 
             * If there is any problem, it will exit at the very first error.
             */
            String dbname = "sob_grup_04";
            String schema = "ROOT";
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            /* this will generate database if not exist */
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/" + dbname, "root", "root");
            Statement stmt = con.createStatement();
            
            /* inserting data */
            /* you have to exclude the id autogenerated from the list of columns if you have use it. */
            String data[] = new String[]{
                 "INSERT INTO " + schema + ".TOPIC VALUES (NEXT VALUE FOR TOPIC_GEN, 'Computer Science')",
                "INSERT INTO " + schema + ".TOPIC VALUES (NEXT VALUE FOR TOPIC_GEN, 'Informatica')",
                "INSERT INTO " + schema + ".TOPIC VALUES (NEXT VALUE FOR TOPIC_GEN, 'Matematicas')",
                "INSERT INTO " + schema + ".TOPIC VALUES (NEXT VALUE FOR TOPIC_GEN, 'Ciencias')",
                "INSERT INTO " + schema + ".TOPIC VALUES (NEXT VALUE FOR TOPIC_GEN, 'Historia')",
                "INSERT INTO " + schema + ".TOPIC VALUES (NEXT VALUE FOR TOPIC_GEN, 'Letras')",
                
                // Usuari 1
                "INSERT INTO " + schema + ".CREDENTIALS VALUES (NEXT VALUE FOR CREDENTIALS_GEN, 'sob', 'sob')",
                "INSERT INTO " + schema + ".USUARI VALUES (NEXT VALUE FOR USUARI_GEN, 'bla@gmail.com', 12, 'Carlos', 1)",
                // Usuari 2
                "INSERT INTO " + schema + ".CREDENTIALS VALUES (NEXT VALUE FOR CREDENTIALS_GEN, 'sob2', 'sob2')",
                "INSERT INTO " + schema + ".USUARI VALUES (NEXT VALUE FOR USUARI_GEN, 'blabla@gmail.com', 15, 'Raul', 2)",
                // Usuari 3
                "INSERT INTO " + schema + ".CREDENTIALS VALUES (NEXT VALUE FOR CREDENTIALS_GEN, 'sob3', 'sob3')",
                "INSERT INTO " + schema + ".USUARI VALUES (NEXT VALUE FOR USUARI_GEN, 'blablabla@gmail.com', 79, 'Elyas', 3)",
                
                // Inserir articles
                "INSERT INTO " + schema + ".ARTICLE VALUES (NEXT VALUE FOR ARTICLE_GEN, '07-12-2024', 'imatge1', 0, 'Resum del article 1', 'Titol del article 1', 0, 1)",
                "INSERT INTO " + schema + ".ARTICLE VALUES (NEXT VALUE FOR ARTICLE_GEN, '06-12-2024', 'imatge2', 1, 'Resum del article 2', 'Titol del article 2', 0, 2)",
                "INSERT INTO " + schema + ".ARTICLE VALUES (NEXT VALUE FOR ARTICLE_GEN, '05-12-2024', 'imatge3', 0, 'Resum del article 3', 'Titol del article 3', 0, 3)",
                "INSERT INTO " + schema + ".ARTICLE VALUES (NEXT VALUE FOR ARTICLE_GEN, '04-12-2024', 'imatge4', 1, 'Resum del article 4', 'Titol del article 4', 0, 1)",
                "INSERT INTO " + schema + ".ARTICLE VALUES (NEXT VALUE FOR ARTICLE_GEN, '03-12-2024', 'imatge5', 0, 'Resum del article 5', 'Titol del article 5', 0, 2)",
                "INSERT INTO " + schema + ".ARTICLE VALUES (NEXT VALUE FOR ARTICLE_GEN, '02-12-2024', 'imatge6', 1, 'Resum del article 6', 'Titol del article 6', 0, 3)",
                "INSERT INTO " + schema + ".ARTICLE VALUES (NEXT VALUE FOR ARTICLE_GEN, '01-12-2024', 'imatge7', 0, 'Resum del article 7', 'Titol del article 7', 0, 1)",
                
                
                
                // Inserir tópics per a cada article
                "INSERT INTO " + schema + ".ARTICLE_TOPIC(article_id, topicos_id) VALUES (1, 1)",
                "INSERT INTO " + schema + ".ARTICLE_TOPIC(article_id, topicos_id) VALUES (1, 2)",
                "INSERT INTO " + schema + ".ARTICLE_TOPIC(article_id, topicos_id) VALUES (2, 3)",
                "INSERT INTO " + schema + ".ARTICLE_TOPIC(article_id, topicos_id) VALUES (2, 4)",
                "INSERT INTO " + schema + ".ARTICLE_TOPIC(article_id, topicos_id) VALUES (3, 5)",
                "INSERT INTO " + schema + ".ARTICLE_TOPIC(article_id, topicos_id) VALUES (3, 6)",
                "INSERT INTO " + schema + ".ARTICLE_TOPIC(article_id, topicos_id) VALUES (4, 1)",
                "INSERT INTO " + schema + ".ARTICLE_TOPIC(article_id, topicos_id) VALUES (4, 2)",
                "INSERT INTO " + schema + ".ARTICLE_TOPIC(article_id, topicos_id) VALUES (5, 3)",
                "INSERT INTO " + schema + ".ARTICLE_TOPIC(article_id, topicos_id) VALUES (5, 4)",
                "INSERT INTO " + schema + ".ARTICLE_TOPIC(article_id, topicos_id) VALUES (6, 5)",
                "INSERT INTO " + schema + ".ARTICLE_TOPIC(article_id, topicos_id) VALUES (6, 6)",
                "INSERT INTO " + schema + ".ARTICLE_TOPIC(article_id, topicos_id) VALUES (7, 1)",
                "INSERT INTO " + schema + ".ARTICLE_TOPIC(article_id, topicos_id) VALUES (7, 2)"
               
                
            };
            for (String datum : data) {
                if (stmt.executeUpdate(datum)<=0) {
                    out.println("<span class='error'>Error inserting data: " + datum + "</span>");
                    return;
                }
                out.println("<pre> -> " + datum + "<pre>");
            }
        %>
        <button onclick="window.location='<%=request.getSession().getServletContext().getContextPath()%>'">Go home</button>
    </body>
</html>