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

    //INSERCIONES A LA BD

    /**
     * Función específica para insertar clientes en la Base de Datos
     *
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
     *
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
     * Insertar el precio para un producto determinado en una fecha determinada. Almacena el histórico de precios en la Base de Datos.
     * <p>
     * La clave de esta tabla es el AutoID.
     *
     * @param codArticulo
     * @param precio
     * @param fecha
     */
    public void insertPrecio(int codArticulo, float precio, LocalDate fecha) {
        //TODO Cambiar los parámetros al objeto

        String insert = "INSERT INTO Historico_Precios values (?, ?, ?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setInt(1, codArticulo);
            stmnt.setFloat(2, precio);
            stmnt.setDate(3, Date.valueOf(fecha));

            stmnt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Función para añadir una compra nueva (No el detalle)
     *
     * @param codigo
     * @param DNI
     * @param fechaCompra
     * @param comentarios
     * @param tipoPago
     */
    public void insertCompra(String codigo, String DNI, LocalDate fechaCompra, String comentarios, int tipoPago) {
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
     *
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
     * Crea los tipos de pago para las compras (Tarjeta, PayPal, efectivo....
     *
     * @param nombre
     * @param descripcion
     */
    public void insertTipoPago(String nombre, String descripcion) {
        //TODO Pasar el objeto correspondiente

        String insert = "INSERT INTO Tipo_Pago (Nombre_Tipo_Pago, Descripcion_Tipo_Pago) values (?, ?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setString(1, nombre);
            stmnt.setString(2, descripcion);

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Productos que interesan al cliente. Una vez el cliente compra un producto que está dentro de su lista de intereses, este pasa a poner su ultimo valor a TRUE
     * mediante un TRIGGER
     *
     * @param codCliente
     * @param codArticulo
     * @param observaciones
     * @param haComprado
     */
    public void insertInteresCliente(String codCliente, int codArticulo, String observaciones, boolean haComprado) {
        //TODO Pasar objeto

        String insert = "INSERT INTO Intereses_Articulos VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setString(1, codCliente);
            stmnt.setInt(2, codArticulo);
            stmnt.setString(3, observaciones);
            stmnt.setBoolean(4, false);

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Primera experiencia con el cliente, donde se le presentan los productos y se hace la demostracion de usos.
     *
     * @param codCliente
     * @param fecha
     * @param direccion
     * @param observaciones
     * @param hayVenta
     */
    public void insertPresentacion(String codCliente, LocalDate fecha, String direccion, String observaciones, boolean hayVenta) {
        //TODO Usar el objeto

        String insert = "INSERT INTO Presentacion (Cod_Cliente, Fecha, Direccion, Observaciones, Venta) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setString(1, codCliente);
            stmnt.setDate(2, Date.valueOf(fecha));
            stmnt.setString(3, direccion);
            stmnt.setString(4, observaciones);
            stmnt.setBoolean(5, hayVenta);

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //CREACION DE LA BD

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
                        "Cod_Compra text NOT NULL PRIMARY KEY," +
                        "Cod_Cliente text NOT NULL," +
                        "Fecha text NOT NULL," +
                        "Observaciones text," +
                        "Tipo_Pago INTEGER," +
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
                        "Comprado INTEGER NOT NULL," +
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
                        "Observaciones text," +
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

        File carpeta = new File(System.getProperty("user.home") + "\\" + ".Joova");

        if (!carpeta.exists()) {
            carpeta.mkdir();
        }


        //TODO Revisar por qué no me deja crear carpeta en user.home
        url = "jdbc:sqlite:" + System.getProperty("user.home") + "\\" + ".Joova\\" + name + ".db";

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

        try {
            conn.prepareStatement("PRAGMA foreign_keys = ON");
            System.out.println("Foreign Keys activadas");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
