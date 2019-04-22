package database;

import Joova.ProductoCardModel;
import NuevoCliente.NuevoClienteModel;
import javafx.beans.property.ListProperty;
import javafx.scene.image.Image;
import model.*;
import nuevoproducto.NuevoProductoModel;
import util.JoovaUtil;

import javax.swing.plaf.nimbus.State;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;

public class HooverDataBase {
    private Connection conn;

    public HooverDataBase(String name) {
        createDatabase(name);
        createTables();
    }

    // MODIFICACIONES DE LA BD

    /**
     * Actualiza el cliente seleccionado de la tabla con los datos de entrada recibidos de el dialogo de edicion.
     */
    public void updateClient(NuevoClienteModel cliente) {
        String update = "UPDATE Cliente SET Nombre = ?, Apellidos = ?, Telefono = ?, Email = ?, Fecha_de_nacimiento = ?, Direccion = ?, Observaciones = ?, Genero = ?, Huerfano = ? WHERE DNI = ?";

        try {
            PreparedStatement stnmt = conn.prepareStatement(update);
            stnmt.setString(1, cliente.getNombreCliente());
            stnmt.setString(2, cliente.getApellidosCliente());
            stnmt.setString(3, cliente.getTelefonoCliente());
            stnmt.setString(4, cliente.getMailCliente());
            stnmt.setString(5, cliente.getNacimientoCliente().toString());
            stnmt.setString(6, cliente.getDireccion());
            stnmt.setString(7, cliente.getObservacionesCliente());
            stnmt.setString(8, cliente.getGenero());
            stnmt.setBoolean(9, cliente.isClienteHuefano());
            stnmt.setString(10, cliente.getDniCliente());

            stnmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //INSERCIONES A LA BD

    /**
     * Función específica para insertar clientes en la Base de Datos
     *
     * @param cliente Objeto con los datos del cliente sacado del controlador del cuadro de dialogo
     */
    public void insertClient(NuevoClienteModel cliente) {
        String insert = "INSERT INTO Cliente VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);
            stmnt.setString(1, cliente.getDniCliente());
            stmnt.setString(2, cliente.getNombreCliente());
            stmnt.setString(3, cliente.getApellidosCliente());
            stmnt.setString(4, cliente.getTelefonoCliente());
            stmnt.setString(5, cliente.getMailCliente());
            stmnt.setString(6, cliente.getNacimientoCliente().toString());
            stmnt.setString(7, cliente.getDireccion());
            stmnt.setString(8, cliente.getObservacionesCliente());
            stmnt.setString(9, cliente.getGenero());
            stmnt.setBoolean(10, cliente.isClienteHuefano());

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Inserta un producto en la base de datos. Devuelve el id del producto insertado o -1 si no se inserta
     *
     * @param model Modelo del controlador del dialogo de entrada de nuevos productos. Contiene todos los datos que el usuario quiere insertar/modificar
     * @return int El codigo del nuevo producto insertado
     */
    public int insertProduct(NuevoProductoModel model) {
        String insert = "INSERT INTO Articulos(Nombre_Articulo, Descripcion, Tipo_Producto, Ruta_Imagen) VALUES (?, ?, ?, ?)";
        int i = -1;
        PreparedStatement stmnt = null;
        try {
            stmnt = conn.prepareStatement(insert);

            stmnt.setString(1, model.getNombreProducto());
            stmnt.setString(2, model.getDescripcionProducto());
            stmnt.setString(3, model.getTipoProducto());
            stmnt.setString(4, model.getDireccionImagen());

            stmnt.executeUpdate();

            String select = "SELECT Cod_Articulo from Articulos where Nombre_Articulo = ?";
            PreparedStatement statement = conn.prepareStatement(select);
            statement.setString(1, model.getNombreProducto());

            ResultSet rs = statement.executeQuery();
            rs.next();
            i = rs.getInt(1);

            stmnt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * Insertar el precio para un producto determinado en una fecha determinada. Almacena el histórico de precios en la Base de Datos.
     * <p>
     * La clave de esta tabla es el AutoID.
     *
     * @param model Modelo con los valores del precio introducido
     */
    public void insertPrecio(PrecioModel model) {
        //TODO Cambiar los parámetros al objeto

        String insert = "INSERT INTO Historico_Precios values (?, ?, ?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setInt(1, model.getCodArticulo());
            stmnt.setDouble(2, model.getPrecioArticulo());
            stmnt.setDate(3, Date.valueOf(model.getFechaCambio()));

            stmnt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Función para añadir una compra nueva (No el detalle)
     *
     * @param model El modelo corespondiente a la vista de la venta realizada
     */
    public void insertCompra(VentasModel model) {
        //TODO Cambiar por el objeto correspondiente

        String insert = "INSERT INTO Compra VALUES(?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);
            stmnt.setString(1, model.getCodContrato());
            stmnt.setString(2, model.getCliente());
            stmnt.setDate(3, Date.valueOf(model.getFechaVenta()));
            stmnt.setString(4, model.getObservacionesVenta());
            stmnt.setInt(5, model.getTipoPago().getCodTipoPago());
            stmnt.setDouble(6, model.getPrecioTotal());

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
     */
    public void insertDetalleCompra(String codCompra, int codArticulo) {
        String insert = "INSERT INTO Detalle_Compra VALUES(?, ?)";
        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);
            stmnt.setString(1, codCompra);
            stmnt.setInt(2, codArticulo);

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
     * @param presentacion
     */
    public void insertPresentacion(PresentacionesModel presentacion) {
        //TODO Usar el objeto

        String insert = "INSERT INTO Presentacion (Cod_Cliente, Fecha, Direccion, Observaciones, Venta) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setString(1, presentacion.getCodCliente());
            stmnt.setString(2, presentacion.getFechaPresentacion().toString());
            stmnt.setString(3, presentacion.getDireccionCliente());
            stmnt.setString(4, presentacion.getObservaciones());
            stmnt.setBoolean(5, presentacion.isVentaRealizada());

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Tabla que recoge las veces que se fue a casa del cliente a poner a funcionar los equipos adquiridos.
     *
     * @param codCliente
     * @param fecha
     * @param observaciones
     */
    public void insertPuestaEnMarcha(String codCliente, LocalDate fecha, String observaciones) {
        //TODO Pasar el objeto

        String insert = "INSERT INTO PuestaMarcha (Cod_Cliente, Fecha, Observaciones) VALUES (?, ?, ?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setString(1, codCliente);
            stmnt.setDate(2, Date.valueOf(fecha));
            stmnt.setString(3, observaciones);

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tabla con los tipos de evento a los que puede asistir el vendedor.
     *
     * @param nombreTipoEvento
     */
    public void insertTipoEvento(String nombreTipoEvento) {
        String insert = "INSERT INTO Tipo_Evento (Nombre_Tipo_Evento) VALUES (?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setString(1, nombreTipoEvento);

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Tabla que contiene las acciones especiales a las que ha acudido el vendedor
     *
     * @param nombreAccion
     * @param fecha
     * @param codTipoEvento
     * @param direccion
     * @param observaciones
     */
    public void insertAccionesEspeciales(String nombreAccion, LocalDate fecha, int codTipoEvento, String direccion, String observaciones) {
        //TODO Pasar el objeto

        String insert = "INSERT INTO Acciones_Especiales (Nombre_Accion_Especial, Fecha, Tipo_Evento, Direccion, Observaciones) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setString(1, nombreAccion);
            stmnt.setDate(2, Date.valueOf(fecha));
            stmnt.setInt(3, codTipoEvento);
            stmnt.setString(4, direccion);
            stmnt.setString(5, observaciones);

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tabla de asistencia de los clientes a cada accion especial
     *
     * @param codAccionEspecial
     * @param codCliente
     * @param observaciones
     * @param hayVenta
     * @param tuvoRegalo
     */
    public void insertAccionesEspecialesClientes(int codAccionEspecial, String codCliente, String observaciones, boolean hayVenta, boolean tuvoRegalo) {
        //TODO Pasar el objeto

        String insert = "INSERT INTO Acciones_Especiales_Clientes VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setInt(1, codAccionEspecial);
            stmnt.setString(2, codCliente);
            stmnt.setString(3, observaciones);
            stmnt.setBoolean(4, hayVenta);
            stmnt.setBoolean(5, tuvoRegalo);

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tabla con las reuniones de varios clientes.
     *
     * @param direccion
     * @param fecha
     * @param observaciones
     */
    public void insertExperiencias(String direccion, LocalDate fecha, String observaciones) {
        //TODO Usar el objeto

        String insert = "INSERT INTO Experiencia (Direccion, Fecha, Observaciones) VALUES (?, ?, ?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setString(1, direccion);
            stmnt.setDate(2, Date.valueOf(fecha));
            stmnt.setString(3, observaciones);

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tabla con los clientes que asisten a una determinada experiencia.
     *
     * @param codExperiencia
     * @param codCliente
     * @param hayVenta
     */
    public void insertExperienciasClientes(int codExperiencia, String codCliente, boolean hayVenta) {
        //TODO Pasar el objeto

        String insert = "INSERT INTO Cliente_Experiencias VALUES(?, ?, ?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setInt(1, codExperiencia);
            stmnt.setString(2, codCliente);
            stmnt.setBoolean(3, hayVenta);

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //ELIMINACION DE DATOS

    /**
     * Borra al cliente del DNI proporcionado de la base de datos. El borrado es en cascada así que cualquier dato relacionado también será borrado de la BD.
     *
     * @param DNI
     */
    public void deleteCliente(String DNI) {
        String delete = "DELETE FROM Cliente WHERE DNI = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(delete);

            stmnt.setString(1, DNI);

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina la compra de la base de datos (Junto con su detalle)
     *
     * @param codCompra
     */
    public void deleteCompra(String codCompra) {
        String delete = "DELETE FROM Compra WHERE Cod_Compra = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(delete);

            stmnt.setString(1, codCompra);

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Borra de la tabla con el detalle de la compra el artículo que coincide tanto el codigo de articulo como del de compra (Las dos claves)
     *
     * @param codCompra
     * @param codArticulo
     */
    public void deleteDetalleCompra(String codCompra, int codArticulo) {
        String delete = "DELETE FROM Detalle_Compra WHERE Cod_Compra = ? AND Cod_Articulo = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(delete);

            stmnt.setString(1, codCompra);
            stmnt.setInt(2, codArticulo);

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Borra el artículo correspondiente al código de artículo proporcionado
     *
     * @param codArticulo
     */
    public void deleteArticulo(int codArticulo) {
        String delete = "DELETE FROM Articulos WHERE Cod_Articulo = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(delete);

            stmnt.setInt(1, codArticulo);

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina el tipo de pago relacionado al código proporcionado.
     * <p>
     * Cuidado con el borrado en cascada, borrar un tipo de pago puede conllevar que se eliminen las compras relacionadas.
     *
     * @param codTipoPago
     */
    public void deleteTipoPago(int codTipoPago) {
        String delete = "DELETE FROM Tipo_Pago WHERE Cod_Tipo_Pago = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(delete);

            stmnt.setInt(1, codTipoPago);

            stmnt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina el precio proporcionado del histórico de precios
     *
     * @param codPrecio
     */
    public void deletePrecio(int codPrecio) {
        String delete = "DELETE FROM Historico_Precios WHERE rowid = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(delete);

            stmnt.setInt(1, codPrecio);

            stmnt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina uno de los intereses que tenga un cliente.
     *
     * @param codCliente
     * @param codArticulo
     */
    public void deleteInteres(String codCliente, int codArticulo) {
        String delete = "DELETE FROM Intereses_Articulos WHERE Cod_Cliente = ? AND Cod_Articulo = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(delete);

            stmnt.setString(1, codCliente);
            stmnt.setInt(2, codArticulo);

            stmnt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina la presentacion concreta hecha a un cliente.
     *
     * @param codPresentacion
     */
    public void deletePresentacion(int codPresentacion) {
        String delete = "DELETE FROM Presentacion WHERE Cod_Presentacion = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(delete);

            stmnt.setInt(1, codPresentacion);

            stmnt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina una puesta en marcha en concreto de la tabla
     *
     * @param codPuestaMarcha
     */
    public void deletePuestaMarcha(int codPuestaMarcha) {
        String delete = "DELETE FROM PuestaMarcha WHERE Cod_Puesta_Marcha = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(delete);

            stmnt.setInt(1, codPuestaMarcha);

            stmnt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina el tipo de evento especificado.
     * <p>
     * Cuidado con la eliminacion en Cascada, eliminar un evento borrara todas las acciones especiales y asistencias de clientes a esa accion relacionadas con el tipo de evento.
     *
     * @param codTipoEvento
     */
    public void deleteTipoEvento(int codTipoEvento) {
        String delete = "DELETE FROM Tipo_Evento WHERE Cod_Tipo_Evento = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(delete);

            stmnt.setInt(1, codTipoEvento);

            stmnt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina la asistencia de un cliente a un evento en particular
     *
     * @param codAccionEspecial
     * @param codCliente
     */
    public void deleteAccionesEspecialesClientes(int codAccionEspecial, String codCliente) {
        String delete = "DELETE FROM Acciones_Especiales_Clientes WHERE Cod_Accion_Especial = ? AND Cod_Cliente = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(delete);

            stmnt.setInt(1, codAccionEspecial);
            stmnt.setString(2, codCliente);

            stmnt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina la Accion Especial llevada a cabo por el Vendedor
     *
     * @param codAccionEspecial
     */
    public void deleteAccionesEspeciales(int codAccionEspecial) {
        String delete = "DELETE FROM Acciones_Especiales WHERE Cod_Accion_Especial = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(delete);

            stmnt.setInt(1, codAccionEspecial);

            stmnt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina la asistencia de un cliente a una accion especial
     *
     * @param codExperiencia
     * @param codCliente
     */
    public void deleteExperienciasCliente(int codExperiencia, String codCliente) {
        String delete = "DELETE FROM Cliente_Experiencias WHERE Cod_Experiencia = ? AND Cod_Cliente = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(delete);

            stmnt.setInt(1, codExperiencia);
            stmnt.setString(2, codCliente);

            stmnt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina la experiencia en la que participó el vendedor
     *
     * @param codExperiencia
     */
    public void deleteExperiencia(int codExperiencia) {
        String delete = "DELETE FROM Experiencia WHERE Cod_Experiencia = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(delete);

            stmnt.setInt(1, codExperiencia);

            stmnt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CONSULTAS A LA BASE DE DATOS

    /**
     * Consulta todos los productos que hay en la base de datos y losguarda en una lista que esta bindeada a la tabla donde se visualizan los clientes.
     *
     * @param listaProductos
     */
    public void consultaTodosProductos(ListProperty<NuevoProductoModel> listaProductos) {
        String query = "SELECT * FROM Articulos";
        ResultSet rs = null;
        try {
            Statement stmnt = conn.createStatement();
            rs = stmnt.executeQuery(query);

            fillListProductos(listaProductos, rs);

            stmnt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Consulta todos los clientes que hay en la base de datos y los guarda en una lista que esta bindeada a la tabla donde se visualizan los clientes.
     *
     * @param listaClientes la lista donde se guardaran los clientes
     */
    public void consultaTodosClientes(ListProperty<ClienteModel> listaClientes) {
        String query = "SELECT * FROM Cliente";
        ResultSet rs = null;
        try {
            Statement stmnt = conn.createStatement();
            rs = stmnt.executeQuery(query);

            fillListClientes(listaClientes, rs);

            stmnt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Consulta todas las presentaciones que hay en la base de datos, y las guarda en la lista que recoge por parametro.
     * @param listaPresentaciones la lista con las presentaciones que se cargaran en la tabla "Presentaciones"
     */
    public void consultaTodasPresentaciones(ListProperty<PresentacionesModel> listaPresentaciones) {
        String query = "select DNI, Nombre, C.Direccion, Fecha, Venta, Presentacion.Observaciones from Presentacion join Cliente C on Presentacion.Cod_Cliente = C.DNI";
        ResultSet rs = null;
        int i = 0;
        try {
            Statement stmnt = conn.createStatement();
            rs = stmnt.executeQuery(query);

            while (rs.next()) {
                listaPresentaciones.add(new PresentacionesModel());
                listaPresentaciones.get(i).setCodCliente(rs.getString(1));
                listaPresentaciones.get(i).setNombreCliente(rs.getString(2));
                listaPresentaciones.get(i).setDireccionCliente(rs.getString(3));
                listaPresentaciones.get(i).setFechaPresentacion(JoovaUtil.stringToLocalDate(rs.getString(4)));
                listaPresentaciones.get(i).setVentaRealizada(rs.getBoolean(5));
                listaPresentaciones.get(i).setObservaciones(rs.getString(6));
                i++;
            }
            stmnt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Consulta todos los clientes de la base de datos que coincidan con el parametro nombre.
     *
     * @param listaClientes
     */
    public void consultaClientesWhere(ListProperty<ClienteModel> listaClientes, String nombre) {
        String query = "SELECT * FROM Cliente WHERE DNI LIKE ? OR Nombre LIKE ? or Apellidos LIKE ? or Telefono like ? or Email like ? or Genero like ? or Huerfano like ?";
        ResultSet rs = null;
        try {
            String busqueda = "%" + nombre + "%";
            PreparedStatement stnmt = conn.prepareStatement(query);
            stnmt.setString(1, busqueda);
            stnmt.setString(2, busqueda);
            stnmt.setString(3, busqueda);
            stnmt.setString(4, busqueda);
            stnmt.setString(5, busqueda);
            stnmt.setString(6, busqueda);
            stnmt.setString(7, busqueda);

            rs = stnmt.executeQuery();

            fillListClientes(listaClientes, rs);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillListProductos(ListProperty<NuevoProductoModel> listaProductos, ResultSet rs) {
        int i = 0;
        ResultSet rsp = null;
        String queryPrecio = "SELECT Precio FROM Historico_Precios WHERE Cod_Articulo = ? ORDER BY Cod_Articulo, Fecha DESC LIMIT 1";
        try {
            while (rs.next()) {
                listaProductos.add(new NuevoProductoModel());

                // Codigo del articulo que queremos buscar en el historico de precios
                int codArticulo = rs.getInt(1);

                PreparedStatement stmntPrecio = conn.prepareStatement(queryPrecio);
                stmntPrecio.setInt(1, codArticulo);
                rsp = stmntPrecio.executeQuery();

                if (rsp.next())
                    listaProductos.get(i).setPrecioProducto(rsp.getDouble(1));
                else
                    listaProductos.get(i).setPrecioProducto(0);


                listaProductos.get(i).setCodArticulo(codArticulo);
                listaProductos.get(i).setNombreProducto(rs.getString(2));
                listaProductos.get(i).setDescripcionProducto(rs.getString(3));
                listaProductos.get(i).setTipoProducto(rs.getString(4));
                listaProductos.get(i).setDireccionImagen(rs.getString(5));
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param listaClientes
     * @param rs
     * @throws SQLException
     */
    private void fillListClientes(ListProperty<ClienteModel> listaClientes, ResultSet rs) throws SQLException {
        int i = 0;
        while (rs.next()) {
            listaClientes.add(new ClienteModel());
            listaClientes.get(i).setDni(rs.getString(1));
            listaClientes.get(i).setNombre(rs.getString(2));
            listaClientes.get(i).setApellidos(rs.getString(3));
            listaClientes.get(i).setTelefono(rs.getString(4));
            listaClientes.get(i).setEmail(rs.getString(5));
            listaClientes.get(i).setFechaNacimiento(JoovaUtil.stringToLocalDate(rs.getString(6)));
            listaClientes.get(i).setDireccion(rs.getString(7));
            listaClientes.get(i).setObservaciones(rs.getString(8));
            listaClientes.get(i).setGenero(rs.getString(9));
            listaClientes.get(i).setHuerfano(rs.getBoolean(10));
            i++;
        }
    }

    public void consultaTodosTiposPago(ListProperty<TipoPagoModel> lista) {
        String query = "SELECT * from Tipo_Pago";
        ResultSet rs = null;

        try {
            Statement stmnt = conn.createStatement();
            rs = stmnt.executeQuery(query);

            int i = 0;
            while (rs.next()) {
                lista.add(new TipoPagoModel());
                lista.get(i).setCodTipoPago(rs.getInt(1));
                lista.get(i).setNombreTipoPago(rs.getString(2));
                lista.get(i).setDescripcionTipoPago(rs.getString(3));
                i++;
            }

            stmnt.close();
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
                        "Fecha_de_nacimiento TEXT," +
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
                        "Total REAL," +
                        "FOREIGN KEY (Cod_Cliente) REFERENCES Cliente (DNI) ON DELETE CASCADE," +
                        "FOREIGN KEY (Tipo_Pago) REFERENCES Tipo_Pago (Cod_Tipo_Pago) ON DELETE CASCADE" +
                        ")";

        String queryDetalleCompra =
                "CREATE TABLE IF NOT EXISTS Detalle_Compra (" +
                        "Cod_Compra text," +
                        "Cod_Articulo INTEGER NOT NULL," +
                        "FOREIGN KEY (Cod_Articulo) REFERENCES Articulos (Cod_Articulo) ON DELETE CASCADE," +
                        "FOREIGN KEY (Cod_Compra) REFERENCES Compra (Cod_Compra) ON DELETE CASCADE" +
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
                        "PRIMARY KEY (Cod_Articulo, Precio, Fecha)," +
                        "FOREIGN KEY (Cod_Articulo) REFERENCES Articulos (Cod_Articulo) ON DELETE CASCADE" +
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
                        "FOREIGN KEY (Cod_Cliente) REFERENCES Cliente (DNI) ON DELETE CASCADE," +
                        "FOREIGN KEY (Cod_Articulo) REFERENCES Articulos (Cod_Articulo) ON DELETE CASCADE" +
                        ")";

        String queryPresentacion =
                "CREATE TABLE IF NOT EXISTS Presentacion (" +
                        "Cod_Presentacion INTEGER PRIMARY KEY," +
                        "Cod_Cliente text NOT NULL," +
                        "Fecha text NOT NULL," +
                        "Direccion text NOT NULL," +
                        "Observaciones text," +
                        "Venta INTEGER NOT NULL," +
                        "FOREIGN KEY (Cod_Cliente) REFERENCES Cliente (DNI) ON DELETE CASCADE" +
                        ")";

        String queryPuestaEnMarcha =
                "CREATE TABLE IF NOT EXISTS PuestaMarcha (" +
                        "Cod_Puesta_Marcha INTEGER PRIMARY KEY," +
                        "Cod_Cliente text NOT NULL," +
                        "Fecha text NOT NULL," +
                        "Observaciones text," +
                        "FOREIGN KEY (Cod_Cliente) REFERENCES Cliente(DNI) ON DELETE CASCADE" +
                        ")";

        String queryAccionesEspecialesCliente =
                "CREATE TABLE IF NOT EXISTS Acciones_Especiales_Clientes (" +
                        "Cod_Accion_Especial INTEGER," +
                        "Cod_Cliente text," +
                        "Observaciones text," +
                        "Venta INTEGER NOT NULL," +
                        "Regalo INTEGER NOT NULL," +
                        "PRIMARY KEY (Cod_Accion_Especial, Cod_Cliente)," +
                        "FOREIGN KEY (Cod_Cliente) REFERENCES Cliente (DNI) ON DELETE CASCADE," +
                        "FOREIGN KEY (Cod_Accion_Especial) REFERENCES Acciones_Especiales (Cod_Accion_Especial) ON DELETE CASCADE" +
                        ")";

        String queryAccionesEspeciales =
                "CREATE TABLE IF NOT EXISTS Acciones_Especiales (" +
                        "Cod_Accion_Especial INTEGER PRIMARY KEY," +
                        "Nombre_Accion_Especial text NOT NULL," +
                        "Fecha text NOT NULL," +
                        "Tipo_Evento INTEGER NOT NULL," +
                        "Direccion text NOT NULL," +
                        "Observaciones text," +
                        "FOREIGN KEY (Tipo_Evento) REFERENCES Tipo_Evento (Cod_Tipo_Evento) ON DELETE CASCADE" +
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
                        "FOREIGN KEY (Cod_Experiencia) REFERENCES Experiencia (Cod_Experiencia) ON DELETE CASCADE," +
                        "FOREIGN KEY (Cod_Cliente) REFERENCES Cliente (DNI) ON DELETE CASCADE" +
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
        String databaseName = name;

        // Lugar donde crearemos la carpeta de la base de datos
        File carpeta = new File(System.getProperty("user.home") + "\\" + ".Joova");

        if (!carpeta.exists()) {
            carpeta.mkdir();
        }

        //Localizacion de la base de datos
        String url = "jdbc:sqlite:" + System.getProperty("user.home") + "\\" + ".Joova\\" + name + ".db";

        //Conexion a la BD
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("El nombre del driver la base de datos es: " + meta.getDriverName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            //Activación de las Foreign Key, para que fuerce los borrados en cascada, o introducción/edicion correcta de datos.
            conn.prepareStatement("PRAGMA foreign_keys = ON");
            System.out.println("Foreign Keys activadas");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Conexion a la BD para consultas externas
     *
     * @return La conexion propia de la base de datos.
     */
    public Connection getConnection() {
        return conn;
    }
}
