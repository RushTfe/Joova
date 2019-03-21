package database;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;

public class HooverDataBase {
    private String url;
    private String databaseName;
    private Connection conn;

    public HooverDataBase(String name) {
        createDatabase(name);
        createTables();
    }

    /**
     * Función específica para insertar clientes en la Base de Datos
     * @param DNI
     * @param nombre
     * @param apellidos
     * @param email
     * @param tlf
     * @param nacimiento
     * @param direccion
     * @param observaciones
     * @param genero
     * @param huerfano
     */
    public void insertClient(String DNI, String nombre, String apellidos, String email, String tlf, LocalDate nacimiento, String direccion, String observaciones, String genero, Boolean huerfano) {
        //TODO Al crear el modelo, pasaremos como dato un objeto de tipo Cliente, y no todos los datos en bruto. Lo mismo con el resto de tablas.
        String insert = "INSERT INTO Cliente VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);
            stmnt.setString(1, DNI);
            stmnt.setString(2, nombre);
            stmnt.setString(3, apellidos);
            stmnt.setString(4, email);
            stmnt.setString(5, tlf);
            stmnt.setDate(6, Date.valueOf(nacimiento));
            stmnt.setString(7, direccion);
            stmnt.setString(8, observaciones);
            stmnt.setString(9, genero);
            stmnt.setBoolean(10, huerfano);

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Inserta un producto en la base de datos
     * @param nombreArt
     * @param descripcion
     * @param tipoProd
     * @param rutaImagen
     */
    public void insertProduct(String nombreArt, String descripcion, String tipoProd, String rutaImagen) {
        //TODO Cambiar los parámetros por el objeto

        String insert = "INSERT INTO Articulos(Nombre_Articulo, Descripcion, Tipo_Producto, Ruta_Imagen) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setString(1, nombreArt);
            stmnt.setString(2, descripcion);
            stmnt.setString(3, tipoProd);
            stmnt.setString(4, rutaImagen);

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Función para añadir una compra nueva (No el detalle)
     * @param codigo
     * @param DNI
     * @param fechaCompra
     * @param comentarios
     * @param tipoPago
     */
    public void insertCompra (String codigo, String DNI, LocalDate fechaCompra, String comentarios, int tipoPago) {
        //TODO Cambiar por el objeto correspondiente

        String insert = "INSERT INTO Compra VALUES(?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);
            stmnt.setString(1, codigo);
            stmnt.setString(2, DNI);
            stmnt.setDate(3, Date.valueOf(fechaCompra));
            stmnt.setString(4, comentarios);
            stmnt.setInt(5, tipoPago);

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Funcion que crea el detalle de la compra realizada
     * @param codCompra
     * @param codArticulo
     * @param cantidad
     */
    public void insertDetalleCompra(String codCompra, int codArticulo, int cantidad) {
        //TODO Pasar el objeto

        String insert = "INSERT INTO Detalle_Compra VALUES(?, ?, ?)";
        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);
            stmnt.setString(1, codCompra);
            stmnt.setInt(2, codArticulo);
            stmnt.setInt(3, cantidad);

            stmnt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Función encargada de crear todas las tablas de la base de datos.
     */
    private void createTables() {
        String queryClientes =
                "CREATE TABLE IF NOT EXISTS Cliente (" +
                        "DNI text PRIMARY KEY," +
                        "Nombre text NOT NULL," +
                        "Apellidos text NOT NULL," +
                        "Telefono text," +
                        "Email text," +
                        "Fecha_de_nacimiento text," +
                        "Direccion text NOT NULL," +
                        "Observaciones text," +
                        "Genero text NOT NULL," +
                        "Huerfano integer NOT NULL" +
                        ")";

        String queryCompra =
                "CREATE TABLE IF NOT EXISTS Compra (" +
                        "Cod_Compra text NOT NULL," +
                        "Cod_Cliente text NOT NULL," +
                        "Fecha text NOT NULL," +
                        "Observaciones text," +
                        "Tipo_Pago INTEGER," +
                        "PRIMARY KEY(Cod_Compra,Cod_Cliente)," +
                        "FOREIGN KEY (Cod_Cliente) REFERENCES Cliente (DNI)," +
                        "FOREIGN KEY (Tipo_Pago) REFERENCES Tipo_Pago (Cod_Tipo_Pago)" +
                        ")";

        String queryDetalleCompra =
                "CREATE TABLE IF NOT EXISTS Detalle_Compra (" +
                        "Cod_Compra text," +
                        "Cod_Articulo INTEGER NOT NULL," +
                        "Cantidad INTEGER NOT NULL," +
                        "PRIMARY KEY (Cod_Compra, Cod_Articulo)," +
                        "FOREIGN KEY (Cod_Articulo) REFERENCES Articulos (Cod_Articulo)," +
                        "FOREIGN KEY (Cod_Compra) REFERENCES Compra (Cod_Compra)" +
                        ")";

        String queryArticulos =
                "CREATE TABLE IF NOT EXISTS Articulos (" +
                        "Cod_Articulo INTEGER PRIMARY KEY," +
                        "Nombre_Articulo text NOT NULL," +
                        "Descripcion text," +
                        "Tipo_Producto text NOT NULL," +
                        "Ruta_Imagen text" +
                        ")";

        String queryHistoricoPrecios =
                "CREATE TABLE IF NOT EXISTS Historico_Precios (" +
                        "Cod_Articulo INTEGER NOT NULL," +
                        "Precio REAL NOT NULL," +
                        "Fecha text NOT NULL," +
                        "FOREIGN KEY (Cod_Articulo) REFERENCES Articulos (Cod_Articulo)" +
                        ")";

        String queryTipoPago =
                "CREATE TABLE IF NOT EXISTS Tipo_Pago (" +
                        "Cod_Tipo_Pago INTEGER PRIMARY KEY," +
                        "Nombre_Tipo_Pago text NOT NULL," +
                        "Descripcion_Tipo_Pago" +
                        ")";

        String queryIntereses =
                "CREATE TABLE IF NOT EXISTS Intereses_Articulos (" +
                        "Cod_Cliente text," +
                        "Cod_Articulo INTEGER," +
                        "Observaciones text," +
                        "PRIMARY KEY (Cod_Cliente, Cod_Articulo)," +
                        "FOREIGN KEY (Cod_Cliente) REFERENCES Cliente (DNI)," +
                        "FOREIGN KEY (Cod_Articulo) REFERENCES Articulos (Cod_Articulo)" +
                        ")";

        String queryPresentacion =
                "CREATE TABLE IF NOT EXISTS Presentacion (" +
                        "Cod_Presentacion INTEGER PRIMARY KEY," +
                        "Cod_Cliente text NOT NULL," +
                        "Fecha text NOT NULL," +
                        "Direccion text NOT NULL," +
                        "Venta INTEGER NOT NULL," +
                        "FOREIGN KEY (Cod_Cliente) REFERENCES Cliente (DNI)" +
                        ")";

        String queryPuestaEnMarcha =
                "CREATE TABLE IF NOT EXISTS PuestaMarcha (" +
                        "Cod_Puesta_Marcha INTEGER PRIMARY KEY," +
                        "Cod_Cliente text NOT NULL," +
                        "Fecha text NOT NULL," +
                        "Observaciones text," +
                        "FOREIGN KEY (Cod_Cliente) REFERENCES Cliente(DNI)" +
                        ")";

        String queryAccionesEspecialesCliente =
                "CREATE TABLE IF NOT EXISTS Acciones_Especiales_Clientes (" +
                        "Cod_Accion_Especial INTEGER," +
                        "Cod_Cliente text," +
                        "Venta INTEGER NOT NULL," +
                        "PRIMARY KEY (Cod_Accion_Especial, Cod_Cliente)," +
                        "FOREIGN KEY (Cod_Cliente) REFERENCES Cliente (DNI)," +
                        "FOREIGN KEY (Cod_Accion_Especial) REFERENCES Acciones_Especiales (Cod_Accion_Especial)" +
                        ")";

        String queryAccionesEspeciales =
                "CREATE TABLE IF NOT EXISTS Acciones_Especiales (" +
                        "Cod_Accion_Especial INTEGER PRIMARY KEY," +
                        "Nombre_Accion_Especial text NOT NULL," +
                        "Fecha text NOT NULL," +
                        "Tipo_Evento INTEGER NOT NULL," +
                        "Direccion text NOT NULL," +
                        "Observaciones text," +
                        "FOREIGN KEY (Tipo_Evento) REFERENCES Tipo_Evento (Cod_Tipo_Evento)" +
                        ")";

        String queryTipoEvento =
                "CREATE TABLE IF NOT EXISTS Tipo_Evento (" +
                        "Cod_Tipo_evento INTEGER PRIMARY KEY," +
                        "Nombre_Tipo_Evento text NOT NULL" +
                        ")";

        String queryClienteExperiencia =
                "CREATE TABLE IF NOT EXISTS Cliente_Experiencias (" +
                        "Cod_Experiencia INTEGER," +
                        "Cod_Cliente text," +
                        "Venta INTEGER NOT NULL," +
                        "PRIMARY KEY (Cod_Experiencia, Cod_Cliente)," +
                        "FOREIGN KEY (Cod_Experiencia) REFERENCES Experiencia (Cod_Experiencia)," +
                        "FOREIGN KEY (Cod_Cliente) REFERENCES Cliente (DNI)" +
                        ")";

        String queryExperiencia =
                "CREATE TABLE IF NOT EXISTS Experiencia (" +
                        "Cod_Experiencia INTEGER PRIMARY KEY," +
                        "Direccion text NOT NULL," +
                        "Fecha text NOT NULL," +
                        "Observaciones" +
                        ")";



        try {
            //Ejecutando sentencias para crear las tablas
            Statement stmnt = conn.createStatement();

            stmnt.execute(queryClientes);
            stmnt.execute(queryCompra);
            stmnt.execute(queryDetalleCompra);
            stmnt.execute(queryArticulos);
            stmnt.execute(queryHistoricoPrecios);
            stmnt.execute(queryTipoPago);
            stmnt.execute(queryIntereses);
            stmnt.execute(queryPresentacion);
            stmnt.execute(queryPuestaEnMarcha);
            stmnt.execute(queryAccionesEspeciales);
            stmnt.execute(queryAccionesEspecialesCliente);
            stmnt.execute(queryTipoEvento);
            stmnt.execute(queryExperiencia);
            stmnt.execute(queryClienteExperiencia);


            System.out.println("Tablas creadas con exito");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Función que crea la base de datos en caso de que esta no exista.
     *
     * @param name nombre de la base de datos. Será el nombre que el usuario introduzca por parámetro.
     */
    public void createDatabase(String name) {
        databaseName = name;

        //TODO Revisar por qué no me deja crear carpeta en user.home
        url = "jdbc:sqlite:" + System.getProperty("user.home") + "\\" + name + ".db";

        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("El nombre del driver la base de datos es: " + meta.getDriverName());
                System.out.println("Se ha creado una nueva base de datos");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
