package database;

import app.JoovaApp;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.*;
import model.NuevoProductoModel;
import util.JoovaUtil;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

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
    public void updateClient(ClienteModel cliente) {
        String update = "UPDATE Cliente SET Nombre = ?, Apellidos = ?, Telefono = ?, Email = ?, Fecha_de_nacimiento = ?, Direccion = ?, Observaciones = ?, Modelo_Aspiradora = ?, Huerfano = ? WHERE DNI = ?";

        try {
            PreparedStatement stnmt = conn.prepareStatement(update);
            stnmt.setString(1, cliente.getNombre());
            stnmt.setString(2, cliente.getApellidos());
            stnmt.setString(3, cliente.getTelefono());
            stnmt.setString(4, cliente.getEmail());
            stnmt.setString(5, cliente.getFechaNacimiento().toString());
            stnmt.setString(6, cliente.getDireccion());
            stnmt.setString(7, cliente.getObservaciones());
            stnmt.setInt(8, cliente.getModeloAspiradora().getCodArticulo());
            stnmt.setBoolean(9, cliente.isHuerfano());
            stnmt.setString(10, cliente.getDni());

            stnmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePuestaMarcha(PMyPresentacionesModel puestaMarcha) {
        String update = "UPDATE PuestaMarcha SET Cod_Cliente = ?, Fecha = ?, Observaciones = ? WHERE Cod_Puesta_Marcha = ?";
        try {
            PreparedStatement stmnt = conn.prepareStatement(update);

            stmnt.setString(1, puestaMarcha.getCodCliente());
            stmnt.setString(2, puestaMarcha.getFechaEvento().toString());
            stmnt.setString(3, puestaMarcha.getObservaciones());
            stmnt.setInt(4, puestaMarcha.getCodigoEvento());

            stmnt.executeUpdate();
            stmnt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePresentacion(PMyPresentacionesModel presentacion) {
        String update = "UPDATE Presentacion SET Cod_Cliente = ?, Fecha = ?, Direccion = ?, Observaciones = ?, Venta = ? WHERE Cod_Presentacion = ?";
        try {
            PreparedStatement stmnt = conn.prepareStatement(update);
            stmnt.setString(1, presentacion.getCodCliente());
            stmnt.setString(2, presentacion.getFechaEvento().toString());
            stmnt.setString(3, presentacion.getDireccionCliente());
            stmnt.setString(4, presentacion.getObservaciones());
            stmnt.setBoolean(5, presentacion.isVentaRealizada());
            stmnt.setInt(6, presentacion.getCodigoEvento());

            stmnt.executeUpdate();
            stmnt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateExperiencia(ExperienciaModel model) {
        String update = "UPDATE Experiencia SET Direccion = ?, Fecha = ?, Observaciones = ? WHERE Cod_Experiencia = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(update);
            stmnt.setString(1, model.getDireccion());
            stmnt.setString(2, model.getFechaExperiencia().toString());
            stmnt.setString(3, model.getObservaciones());
            stmnt.setInt(4, model.getCodExperiencia());

            stmnt.executeUpdate();
            stmnt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateExperienciaCliente(ListProperty<Participante> listaParticipantes, int codExperiencia) {
        String update = "INSERT INTO Cliente_Experiencias(Cod_Experiencia, Cod_Cliente, Venta) VALUES (?, ?, ?)";

        try {
            for (int i = 0; i < listaParticipantes.size(); i++) {
                deleteExperienciasCliente(codExperiencia, listaParticipantes.get(i).getCliente().getDni());

                PreparedStatement stmnt = conn.prepareStatement(update);
                stmnt.setInt(1, codExperiencia);
                stmnt.setString(2, listaParticipantes.get(i).getCliente().getDni());
                stmnt.setBoolean(3, listaParticipantes.get(i).isCompra());

                stmnt.executeUpdate();
                stmnt.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAccionEspecial(AccionEspecialModel model) {
        String update = "UPDATE Acciones_Especiales SET Nombre_Accion_Especial = ?, Fecha = ?, Tipo_Evento = ?, Direccion = ?, Observaciones = ? WHERE Cod_Accion_Especial = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(update);
            stmnt.setString(1, model.getNombreEvento());
            stmnt.setString(2, model.getFechaEvento().toString());
            stmnt.setInt(3, model.getTipoEvento().getCodTipoPago());
            stmnt.setString(4, model.getDireccionEvento());
            stmnt.setString(5, model.getObservacionesEvento());
            stmnt.setInt(6, model.getCodEvento());

            stmnt.executeUpdate();
            stmnt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAccionEspecialCliente(ListProperty<Participante> participantes, int codEvento) {
        //Hacemos un insert porque el usuario podria meter nuevos clientes al actualizar, y no se puede actualizar algo que no está
        String insert = "INSERT INTO Acciones_Especiales_Clientes (Cod_Accion_Especial, Cod_Cliente, Observaciones, Venta, Regalo) VALUES (?, ?, ?, ?, ?)";

        try {
            for (int i = 0; i < participantes.size(); i++) {
                // Así que primero los borramos todos, y luego los volvemos a meter desde la tabla que recibimos
                deleteAccionesEspecialesClientes(codEvento, participantes.get(i).getCliente().getDni());

                PreparedStatement stmnt = conn.prepareStatement(insert);
                stmnt.setInt(1, codEvento);
                stmnt.setString(2, participantes.get(i).getCliente().getDni());
                stmnt.setString(3, participantes.get(i).getObservacionParticipante());
                stmnt.setBoolean(4, participantes.get(i).isCompra());
                stmnt.setBoolean(5, participantes.get(i).isRegalo());
                stmnt.executeUpdate();
                stmnt.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProducto(NuevoProductoModel aModificar) {
        String update = "UPDATE Articulos " +
                "SET Nombre_Articulo = ?," +
                "Descripcion = ?, " +
                "Tipo_Producto = ?, " +
                "Ruta_Imagen = ? " +
                "WHERE Cod_Articulo = ?";

        try {
            // Actualizacion del articulo
            PreparedStatement stmnt = conn.prepareStatement(update);
            stmnt.setString(1, aModificar.getNombreProducto());
            stmnt.setString(2, aModificar.getDescripcionProducto());
            stmnt.setString(3, aModificar.getTipoProducto());
            stmnt.setString(4, aModificar.getDireccionImagen());
            stmnt.setInt(5, aModificar.getCodArticulo());

            stmnt.executeUpdate();
            stmnt.close();

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
    public void insertClient(ClienteModel cliente) {
        String insert = "INSERT INTO Cliente VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);
            stmnt.setString(1, cliente.getDni());
            stmnt.setString(2, cliente.getNombre());
            stmnt.setString(3, cliente.getApellidos());
            stmnt.setString(4, cliente.getTelefono());
            stmnt.setString(5, cliente.getEmail());
            stmnt.setString(6, cliente.getFechaNacimiento().toString());
            stmnt.setString(7, cliente.getDireccion());
            stmnt.setString(8, cliente.getObservaciones());
            stmnt.setInt(9, cliente.getModeloAspiradora().getCodArticulo());
            stmnt.setBoolean(10, cliente.isHuerfano());

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
    public void insertPrecio(PrecioModel model) throws SQLException {
        //TODO Cambiar los parámetros al objeto

        String insert = "INSERT INTO Historico_Precios values (?, ?, ?, ?)";
         PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setInt(1, model.getCodArticulo());
            stmnt.setDouble(2, model.getPrecioArticulo());
            stmnt.setString(3, model.getFechaCambio().toString());
            stmnt.setString(4, String.valueOf(System.currentTimeMillis()));

            stmnt.executeUpdate();

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
            stmnt.setString(3, model.getFechaVenta().toString());
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

    public void insertInteresCliente(InteresesModel interesesModel) throws SQLException {
        //TODO Pasar objeto

        String insert = "INSERT INTO Intereses_Articulos VALUES (?, ?)";

            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setString(1, interesesModel.getCodCliente());
            stmnt.setInt(2, interesesModel.getArticulo().getCodArticulo());

            stmnt.executeUpdate();
    }

    /**
     * Primera experiencia con el cliente, donde se le presentan los productos y se hace la demostracion de usos.
     *
     * @param presentacion
     */
    public void insertPresentacion(PMyPresentacionesModel presentacion) {
        //TODO Usar el objeto

        String insert = "INSERT INTO Presentacion (Cod_Cliente, Fecha, Direccion, Observaciones, Venta) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setString(1, presentacion.getCodCliente());
            stmnt.setString(2, presentacion.getFechaEvento().toString());
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
     * @param model El modelo de Puestas en marcha y presentaciones
     */
    public void insertPuestaEnMarcha(PMyPresentacionesModel model) {
        //TODO Pasar el objeto

        String insert = "INSERT INTO PuestaMarcha (Cod_Cliente, Fecha, Observaciones) VALUES (?, ?, ?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setString(1, model.getCodCliente());
            stmnt.setString(2, model.getFechaEvento().toString());
            stmnt.setString(3, model.getObservaciones());

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
     */
    public int insertAccionesEspeciales(AccionEspecialModel model) {
        //TODO Pasar el objeto

        String insert = "INSERT INTO Acciones_Especiales (Nombre_Accion_Especial, Fecha, Tipo_Evento, Direccion, Observaciones) VALUES (?, ?, ?, ?, ?)";

        int codigo = -1;
        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setString(1, model.getNombreEvento());
            stmnt.setString(2, model.getFechaEvento().toString());
            stmnt.setInt(3, model.getTipoEvento().getCodTipoPago());
            stmnt.setString(4, model.getDireccionEvento());
            stmnt.setString(5, model.getObservacionesEvento());
            stmnt.executeUpdate();

            stmnt.close();

            String query = "SELECT Cod_Accion_Especial from Acciones_Especiales " +
                    "WHERE Fecha = '" + model.getFechaEvento().toString() + "' " +
                    "and Direccion = '" + model.getDireccionEvento() + "' " +
                    "and Nombre_Accion_Especial = '" + model.getNombreEvento() + "'";
            PreparedStatement stmntCodigo = conn.prepareStatement(query);

            ResultSet rsCodigo = stmntCodigo.executeQuery();
            rsCodigo.next();
            codigo = rsCodigo.getInt(1);
            stmntCodigo.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return codigo;
    }

    /**
     * Tabla de asistencia de los clientes a cada accion especial
     *
     * @param codAccionEspecial
     */
    public void insertAccionesEspecialesClientes(int codAccionEspecial, Participante participante) {
        //TODO Pasar el objeto

        String insert = "INSERT INTO Acciones_Especiales_Clientes VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setInt(1, codAccionEspecial);
            stmnt.setString(2, participante.getCliente().getDni());
            stmnt.setString(3, participante.getObservacionParticipante());
            stmnt.setBoolean(4, participante.isCompra());
            stmnt.setBoolean(5, participante.isRegalo());

            stmnt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tabla con las reuniones de varios clientes.
     */
    public int insertExperiencias(ExperienciaModel experiencia) {
        //TODO Usar el objeto

        String insert = "INSERT INTO Experiencia (Direccion, Fecha, Observaciones) VALUES (?, ?, ?)";

        int codigo = -1;

        try {
            PreparedStatement stmnt = conn.prepareStatement(insert);

            stmnt.setString(1, experiencia.getDireccion());
            stmnt.setString(2, experiencia.getFechaExperiencia().toString());
            stmnt.setString(3, experiencia.getObservaciones());

            stmnt.executeUpdate();
            stmnt.close();

            String select = "SELECT Cod_Experiencia from Experiencia where Direccion = '" + experiencia.getDireccion() + "' AND Fecha = '" + experiencia.getFechaExperiencia().toString() + "'";
            Statement selectCod = conn.createStatement();
            ResultSet rs = selectCod.executeQuery(select);

            rs.next();
            codigo = rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return codigo;
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
    public void deletePrecio(PrecioModel model) {
        String delete = "DELETE FROM Historico_Precios WHERE Cod_Articulo = ? AND Precio = ? AND Fecha = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(delete);

            stmnt.setInt(1, model.getCodArticulo());
            stmnt.setDouble(2, model.getPrecioArticulo());
            stmnt.setString(3, model.getFechaCambio().toString());

            stmnt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina uno de los intereses que tenga un cliente.
     */
    public void deleteInteres(InteresesModel interesesModel) {
        String delete = "DELETE FROM Intereses_Articulos WHERE Cod_Cliente = ? AND Cod_Articulo = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(delete);

            stmnt.setString(1, interesesModel.getCodCliente());
            stmnt.setInt(2, interesesModel.getArticulo().getCodArticulo());

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

    public void consultaProductosWhere(ListProperty<NuevoProductoModel> listaProductos, String text) {
        String query = "SELECT * FROM Articulos JOIN Historico_Precios HP on Articulos.Cod_Articulo = HP.Cod_Articulo " +
                "WHERE Nombre_Articulo LIKE ? " +
                "OR Tipo_Producto LIKE ?" +
                "ORDER BY Instants DESC";

        String busqueda = "%" + text + "%";

        try {
            PreparedStatement stmnt = conn.prepareStatement(query);
            stmnt.setString(1, busqueda);
            stmnt.setString(2, busqueda);
            ResultSet rs = stmnt.executeQuery();
            int i = 0;

            while (rs.next()) {
                listaProductos.add(new NuevoProductoModel());
                listaProductos.get(i).setCodArticulo(rs.getInt(1));
                listaProductos.get(i).setNombreProducto(rs.getString(2));
                listaProductos.get(i).setDescripcionProducto(rs.getString(3));
                listaProductos.get(i).setTipoProducto(rs.getString(4));
                listaProductos.get(i).setDireccionImagen(rs.getString(5));
                listaProductos.get(i).setPrecioProducto(rs.getDouble(7));
                listaProductos.get(i).setImagen(new ImageView(new Image(rs.getString(5))));
                listaProductos.get(i).getImagen().setFitHeight(200);
                listaProductos.get(i).getImagen().setFitWidth(200);

                i++;
            }
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
        String query = "SELECT * FROM Cliente JOIN Articulos A on Cliente.Modelo_Aspiradora = A.Cod_Articulo";
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

    public void consultaVentasCliente(ListProperty<VentasModel> listaVentas, String codCliente) {
        String query = "SELECT * FROM Compra JOIN Tipo_Pago TP on Compra.Tipo_Pago = TP.Cod_Tipo_Pago WHERE Cod_Cliente = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(query);
            stmnt.setString(1, codCliente);
            ResultSet rs = stmnt.executeQuery();
            int i = 0;

            while (rs.next()) {
                TipoPagoyEventoModel tipoPago = new TipoPagoyEventoModel();
                tipoPago.setCodTipoPago(rs.getInt(7));
                tipoPago.setNombreTipoPago(rs.getString(8));
                tipoPago.setDescripcionTipoPago(rs.getString(9));
                listaVentas.add(new VentasModel());
                listaVentas.get(i).setCodContrato(rs.getString(1));
                listaVentas.get(i).setCliente(rs.getString(2));
                listaVentas.get(i).setFechaVenta(JoovaUtil.stringToLocalDate(rs.getString(3)));
                listaVentas.get(i).setObservacionesVenta(rs.getString(4));
                listaVentas.get(i).setTipoPago(tipoPago);
                listaVentas.get(i).setPrecioTotal(rs.getDouble(6));
                i++;
            }

            stmnt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void consultaVentasClienteWhere(ListProperty<VentasModel> listaVentas, String texto, String dni) {
        String query = "SELECT * FROM Compra " +
                "JOIN Tipo_Pago TP on Compra.Tipo_Pago = TP.Cod_Tipo_Pago " +
                "WHERE (Cod_Compra LIKE ? " +
                "OR Fecha LIKE ?)" +
                "AND Cod_Cliente = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(query);
            String busqueda = "%" + texto + "%";
            stmnt.setString(1, busqueda);
            stmnt.setString(2, busqueda);
            stmnt.setString(3, dni);
            ResultSet rs = stmnt.executeQuery();
            int i = 0;

            while (rs.next()) {
                TipoPagoyEventoModel tipoPago = new TipoPagoyEventoModel();
                tipoPago.setCodTipoPago(rs.getInt(7));
                tipoPago.setNombreTipoPago(rs.getString(8));
                tipoPago.setDescripcionTipoPago(rs.getString(9));
                listaVentas.add(new VentasModel());
                listaVentas.get(i).setCodContrato(rs.getString(1));
                listaVentas.get(i).setCliente(rs.getString(2));
                listaVentas.get(i).setFechaVenta(JoovaUtil.stringToLocalDate(rs.getString(3)));
                listaVentas.get(i).setObservacionesVenta(rs.getString(4));
                listaVentas.get(i).setTipoPago(tipoPago);
                listaVentas.get(i).setPrecioTotal(rs.getDouble(6));
                i++;
            }
            stmnt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Consulta todas las presentaciones que hay en la base de datos, y las guarda en la lista que recoge por parametro.
     *
     * @param listaPresentaciones la lista con las presentaciones que se cargaran en la tabla "Presentaciones"
     */
    public void consultaTodasPresentaciones(ListProperty<PMyPresentacionesModel> listaPresentaciones) {
        String query = "select DNI, Nombre, C.Direccion, Fecha, Venta, Presentacion.Observaciones, Cod_Presentacion from Presentacion join Cliente C on Presentacion.Cod_Cliente = C.DNI";
        ResultSet rs = null;
        int i = 0;
        try {
            Statement stmnt = conn.createStatement();
            rs = stmnt.executeQuery(query);

            while (rs.next()) {
                listaPresentaciones.add(new PMyPresentacionesModel());
                listaPresentaciones.get(i).setCodCliente(rs.getString(1));
                listaPresentaciones.get(i).setNombreCliente(rs.getString(2));
                listaPresentaciones.get(i).setDireccionCliente(rs.getString(3));
                listaPresentaciones.get(i).setFechaEvento(JoovaUtil.stringToLocalDate(rs.getString(4)));
                listaPresentaciones.get(i).setVentaRealizada(rs.getBoolean(5));
                listaPresentaciones.get(i).setObservaciones(rs.getString(6));
                listaPresentaciones.get(i).setCodigoEvento(rs.getInt(7));
                i++;
            }
            stmnt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void consultaPresentacionCliente(ListProperty<PMyPresentacionesModel> listaPresentaciones, String codCliente) {
        String query = "SELECT * FROM Presentacion WHERE Cod_Cliente = ?";
        try {
            PreparedStatement stmnt = conn.prepareStatement(query);
            stmnt.setString(1, codCliente);
            ResultSet rs = stmnt.executeQuery();
            int i = 0;

            while (rs.next()) {
                listaPresentaciones.add(new PMyPresentacionesModel());
                listaPresentaciones.get(i).setCodigoEvento(rs.getInt(1));
                listaPresentaciones.get(i).setCodCliente(rs.getString(2));
                listaPresentaciones.get(i).setFechaEvento(JoovaUtil.stringToLocalDate(rs.getString(3)));
                listaPresentaciones.get(i).setDireccionCliente(rs.getString(4));
                listaPresentaciones.get(i).setObservaciones(rs.getString(5));
                listaPresentaciones.get(i).setVentaRealizada(rs.getBoolean(6));
                i++;
            }

            stmnt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void consultaTodasPuestasMarcha(ListProperty<PMyPresentacionesModel> listaPuestasMarcha) {
        String query = "SELECT Cod_Puesta_Marcha, Cod_Cliente, Nombre, Fecha, PuestaMarcha.Observaciones FROM PuestaMarcha JOIN Cliente C on PuestaMarcha.Cod_Cliente = C.DNI";
        ResultSet rs = null;
        int i = 0;
        try {
            Statement stmnt = conn.createStatement();
            rs = stmnt.executeQuery(query);

            while (rs.next()) {
                listaPuestasMarcha.add(new PMyPresentacionesModel());
                listaPuestasMarcha.get(i).setCodigoEvento(rs.getInt(1));
                listaPuestasMarcha.get(i).setCodCliente(rs.getString(2));
                listaPuestasMarcha.get(i).setNombreCliente(rs.getString(3));
                listaPuestasMarcha.get(i).setFechaEvento(JoovaUtil.stringToLocalDate(rs.getString(4)));
                listaPuestasMarcha.get(i).setObservaciones(rs.getString(5));
                i++;
            }
            stmnt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void consultaPuestaMarchaCliente(ListProperty<PMyPresentacionesModel> listaPuestasMarcha, String codCliente) {
        String query = "SELECT * FROM PuestaMarcha WHERE Cod_Cliente = ?";
        try {
            PreparedStatement stmnt = conn.prepareStatement(query);
            stmnt.setString(1, codCliente);
            ResultSet rs = stmnt.executeQuery();
            int i = 0;

            while (rs.next()) {
                listaPuestasMarcha.add(new PMyPresentacionesModel());
                listaPuestasMarcha.get(i).setCodigoEvento(rs.getInt(1));
                listaPuestasMarcha.get(i).setCodCliente(rs.getString(2));
                listaPuestasMarcha.get(i).setFechaEvento(JoovaUtil.stringToLocalDate(rs.getString(3)));
                listaPuestasMarcha.get(i).setObservaciones(rs.getString(4));
                i++;
            }

            stmnt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void consultaTodasExperiencias(ListProperty<ExperienciaModel> listaExperiencias) {
        String query = "SELECT * FROM Experiencia";
        ResultSet rs = null;
        int i = 0;
        try {
            Statement stmnt = conn.createStatement();
            rs = stmnt.executeQuery(query);

            while (rs.next()) {
                listaExperiencias.add(new ExperienciaModel());
                listaExperiencias.get(i).setCodExperiencia(rs.getInt(1));
                listaExperiencias.get(i).setDireccion(rs.getString(2));
                listaExperiencias.get(i).setFechaExperiencia(JoovaUtil.stringToLocalDate(rs.getString(3)));
                listaExperiencias.get(i).setObservaciones(rs.getString(4));
                i++;
            }
            stmnt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void consultaExperienciasCodCliente(ListProperty<ExperienciaModel> listaExperiencias, String codCliente) {
        String query = "SELECT * FROM Experiencia " +
                "JOIN Cliente_Experiencias CE on Experiencia.Cod_Experiencia = CE.Cod_Experiencia " +
                "WHERE Cod_Cliente = ?";
        int i = 0;
        try {
            PreparedStatement stmnt = conn.prepareStatement(query);
            stmnt.setString(1, codCliente);
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                listaExperiencias.add(new ExperienciaModel());
                listaExperiencias.get(i).setCodExperiencia(rs.getInt(1));
                listaExperiencias.get(i).setDireccion(rs.getString(2));
                listaExperiencias.get(i).setFechaExperiencia(JoovaUtil.stringToLocalDate(rs.getString(3)));
                listaExperiencias.get(i).setObservaciones(rs.getString(4));
                i++;
            }
            stmnt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void constulaTodosEventos(ListProperty<TipoPagoyEventoModel> listaEventos) {
        String query = "SELECT * FROM Tipo_Evento";
        try {
            Statement stmnt = conn.createStatement();
            ResultSet rs = stmnt.executeQuery(query);
            int i = 0;

            while (rs.next()) {
                listaEventos.add(new TipoPagoyEventoModel());
                listaEventos.get(i).setCodTipoPago(rs.getInt(1));
                listaEventos.get(i).setNombreTipoPago(rs.getString(2));
                i++;
            }
            stmnt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void consultaTodasAccionesEspeciales(ListProperty<AccionEspecialModel> listaAcciones) {
        String query = "SELECT * FROM Acciones_Especiales";
        try {
            Statement stmnt = conn.createStatement();
            ResultSet rs = stmnt.executeQuery(query);
            int i = 0;

            while (rs.next()) {
                listaAcciones.add(new AccionEspecialModel());
                listaAcciones.get(i).setCodEvento(rs.getInt(1));
                listaAcciones.get(i).setNombreEvento(rs.getString(2));
                listaAcciones.get(i).setFechaEvento(JoovaUtil.stringToLocalDate(rs.getString(3)));
                listaAcciones.get(i).setTipoEvento(consultaEventoCod(rs.getInt(4)));
                listaAcciones.get(i).setDireccionEvento(rs.getString(5));
                listaAcciones.get(i).setObservacionesEvento(rs.getString(6));
                i++;
            }
            stmnt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void consultaAccionesEspecialesCodCliente(ListProperty<AccionEspecialModel> listaAcciones, String codCliente) {
        String query = "SELECT * FROM Acciones_Especiales " +
                "JOIN Acciones_Especiales_Clientes AEC on Acciones_Especiales.Cod_Accion_Especial = AEC.Cod_Accion_Especial " +
                "JOIN Tipo_Evento TE on Acciones_Especiales.Tipo_Evento = TE.Cod_Tipo_evento " +
                "WHERE Cod_Cliente = ?";

        try {
            PreparedStatement stmnt = conn.prepareStatement(query);
            stmnt.setString(1, codCliente);
            ResultSet rs = stmnt.executeQuery();
            int i = 0;

            while (rs.next()) {
                TipoPagoyEventoModel tipoEvento = new TipoPagoyEventoModel();
                tipoEvento.setCodTipoPago(rs.getInt(12));
                tipoEvento.setNombreTipoPago(rs.getString(13));
                listaAcciones.add(new AccionEspecialModel());
                listaAcciones.get(i).setCodEvento(rs.getInt(1));
                listaAcciones.get(i).setNombreEvento(rs.getString(2));
                listaAcciones.get(i).setFechaEvento(JoovaUtil.stringToLocalDate(rs.getString(3)));
                listaAcciones.get(i).setTipoEvento(tipoEvento);
                listaAcciones.get(i).setDireccionEvento(rs.getString(5));
                listaAcciones.get(i).setObservacionesEvento(rs.getString(6));
                i++;
            }
            stmnt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void consultaInteresesCliente(ListProperty<InteresesModel> listaIntereses, String codCliente) {
        String query = "SELECT * FROM Intereses_Articulos JOIN Articulos A on Intereses_Articulos.Cod_Articulo = A.Cod_Articulo WHERE Cod_Cliente = ?";
        int i = 0;

        try {
            PreparedStatement stmnt = conn.prepareStatement(query);
            stmnt.setString(1, codCliente);
            ResultSet rs = stmnt.executeQuery();

            while (rs.next()) {
                NuevoProductoModel nuevoProductoModel = new NuevoProductoModel();
                nuevoProductoModel.setCodArticulo(rs.getInt(3));
                nuevoProductoModel.setNombreProducto(rs.getString(4));
                nuevoProductoModel.setDescripcionProducto(rs.getString(5));
                nuevoProductoModel.setTipoProducto(rs.getString(6));
                nuevoProductoModel.setDireccionImagen(rs.getString(7));
                listaIntereses.add(new InteresesModel());
                listaIntereses.get(i).setCodCliente(rs.getString(1));
                listaIntereses.get(i).setArticulo(nuevoProductoModel);
                i++;
            }
            stmnt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ListProperty<Participante> consultaAccionesEspecialesClientesCodigo(int codigoAccion) {
        ListProperty<Participante> listaParticipantes = new SimpleListProperty<>(this, "listaParticipantes", FXCollections.observableArrayList());

        String query = "SELECT * FROM Acciones_Especiales_Clientes join Cliente C on Acciones_Especiales_Clientes.Cod_Cliente = C.DNI JOIN Articulos A on C.Modelo_Aspiradora = A.Cod_Articulo WHERE Cod_Accion_Especial = ?";
        try {
            PreparedStatement stmnt = conn.prepareStatement(query);
            stmnt.setInt(1, codigoAccion);
            ResultSet rs = stmnt.executeQuery();
            int i = 0;

            while (rs.next()) {
                NuevoProductoModel nuevoProductoModel = new NuevoProductoModel();
                nuevoProductoModel.setCodArticulo(rs.getInt(16));
                nuevoProductoModel.setNombreProducto(rs.getString(17));
                nuevoProductoModel.setDescripcionProducto(rs.getString(18));
                nuevoProductoModel.setTipoProducto(rs.getString(19));
                nuevoProductoModel.setDireccionImagen(rs.getString(20));
                ClienteModel clienteModel = new ClienteModel();
                clienteModel.setDni(rs.getString(6));
                clienteModel.setNombre(rs.getString(7));
                clienteModel.setApellidos(rs.getString(8));
                clienteModel.setTelefono(rs.getString(9));
                clienteModel.setEmail(rs.getString(10));
                clienteModel.setFechaNacimiento(JoovaUtil.stringToLocalDate(rs.getString(11)));
                clienteModel.setDireccion(rs.getString(12));
                clienteModel.setObservaciones(rs.getString(13));
                clienteModel.setModeloAspiradora(nuevoProductoModel);
                clienteModel.setHuerfano(rs.getBoolean(15));

                listaParticipantes.add(new Participante());
                listaParticipantes.get(i).setCliente(clienteModel);
                listaParticipantes.get(i).setObservacionParticipante(rs.getString(3));
                listaParticipantes.get(i).setCompra(rs.getBoolean(4));
                listaParticipantes.get(i).setRegalo(rs.getBoolean(5));
                i++;
            }
            stmnt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaParticipantes;
    }

    public TipoPagoyEventoModel consultaEventoCod(int codEvento) {
        TipoPagoyEventoModel model = new TipoPagoyEventoModel();
        String query = "SELECT * FROM Tipo_Evento WHERE Cod_Tipo_evento = ?";
        ResultSet rs = null;
        try {
            PreparedStatement stmnt = conn.prepareStatement(query);
            stmnt.setInt(1, codEvento);

            rs = stmnt.executeQuery();
            rs.next();

            model.setCodTipoPago(rs.getInt(1));
            model.setNombreTipoPago(rs.getString(2));

            stmnt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }

    public ListProperty<Participante> consultaExperienciaSegunCodigo(int codExperiencia) {
        String query = "SELECT * FROM Cliente_Experiencias WHERE Cod_Experiencia = ?";
        ListProperty<Participante> listaParticipantes = new SimpleListProperty<>(this, "listaParticipantes", FXCollections.observableArrayList());
        try {
            PreparedStatement stmnt = conn.prepareStatement(query);
            stmnt.setInt(1, codExperiencia);
            ResultSet rs = stmnt.executeQuery();

            while (rs.next()) {
                String dni = rs.getString(2);
                Participante participante = new Participante();
                participante.setCliente(consultaClienteDNI(dni));
                participante.setCompra(rs.getBoolean(3));
                listaParticipantes.add(participante);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaParticipantes;
    }

    public ClienteModel consultaClienteDNI(String dni) {
        ClienteModel clienteModel = new ClienteModel();
        try {
            String queryCliente = "Select * from Cliente JOIN Articulos A on Cliente.Modelo_Aspiradora = A.Cod_Articulo where DNI = '" + dni + "'";
            Statement stmntCliente = conn.createStatement();
            ResultSet rsCliente = stmntCliente.executeQuery(queryCliente);

            rsCliente.next();
            clienteModel.setDni(dni);
            NuevoProductoModel nuevoProductoModel = new NuevoProductoModel();
            nuevoProductoModel.setCodArticulo(rsCliente.getInt(11));
            nuevoProductoModel.setNombreProducto(rsCliente.getString(12));
            nuevoProductoModel.setDescripcionProducto(rsCliente.getString(13));
            nuevoProductoModel.setTipoProducto(rsCliente.getString(14));
            nuevoProductoModel.setDescripcionProducto(rsCliente.getString(15));
            clienteModel.setNombre(rsCliente.getString(2));
            clienteModel.setApellidos(rsCliente.getString(3));
            clienteModel.setTelefono(rsCliente.getString(4));
            clienteModel.setEmail(rsCliente.getString(5));
            clienteModel.setFechaNacimiento(JoovaUtil.stringToLocalDate(rsCliente.getString(6)));
            clienteModel.setDireccion(rsCliente.getString(7));
            clienteModel.setObservaciones(rsCliente.getString(8));
            clienteModel.setModeloAspiradora(nuevoProductoModel);
            clienteModel.setHuerfano(rsCliente.getBoolean(10));

            stmntCliente.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clienteModel;
    }

    /**
     * Consulta todos los clientes de la base de datos que coincidan con el parametro nombre.
     *
     * @param listaClientes
     */
    public void consultaClientesWhere(ListProperty<ClienteModel> listaClientes, String nombre) {
        String query = "SELECT * FROM Cliente JOIN Articulos A on Cliente.Modelo_Aspiradora = A.Cod_Articulo WHERE DNI LIKE ? OR Nombre LIKE ? or Apellidos LIKE ? or Telefono like ? or Email like ? or Nombre_Articulo like ? or Huerfano like ?";
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
        String queryPrecio = "SELECT Precio FROM Historico_Precios WHERE Cod_Articulo = ? ORDER BY Instants DESC LIMIT 1";
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
                listaProductos.get(i).setImagen(new ImageView(new Image(rs.getString(5))));
                listaProductos.get(i).getImagen().setFitWidth(200);
                listaProductos.get(i).getImagen().setFitHeight(200);
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
            NuevoProductoModel nuevoProductoModel = new NuevoProductoModel();
            nuevoProductoModel.setCodArticulo(rs.getInt(11));
            nuevoProductoModel.setNombreProducto(rs.getString(12));
            nuevoProductoModel.setDescripcionProducto(rs.getString(13));
            nuevoProductoModel.setTipoProducto(rs.getString(14));
            nuevoProductoModel.setDireccionImagen(rs.getString(15));
            listaClientes.add(new ClienteModel());
            listaClientes.get(i).setDni(rs.getString(1));
            listaClientes.get(i).setNombre(rs.getString(2));
            listaClientes.get(i).setApellidos(rs.getString(3));
            listaClientes.get(i).setTelefono(rs.getString(4));
            listaClientes.get(i).setEmail(rs.getString(5));
            listaClientes.get(i).setFechaNacimiento(JoovaUtil.stringToLocalDate(rs.getString(6)));
            listaClientes.get(i).setDireccion(rs.getString(7));
            listaClientes.get(i).setObservaciones(rs.getString(8));
            listaClientes.get(i).setModeloAspiradora(nuevoProductoModel);
            listaClientes.get(i).setHuerfano(rs.getBoolean(10));
            i++;
        }
    }

    public void consultaTodosTiposPago(ListProperty<TipoPagoyEventoModel> lista) {
        String query = "SELECT * from Tipo_Pago";
        ResultSet rs = null;

        try {
            Statement stmnt = conn.createStatement();
            rs = stmnt.executeQuery(query);

            int i = 0;
            while (rs.next()) {
                lista.add(new TipoPagoyEventoModel());
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
                        "Modelo_Aspiradora INTEGER NOT NULL," +
                        "Huerfano integer NOT NULL," +
                        "FOREIGN KEY (Modelo_Aspiradora) REFERENCES Articulos(Cod_Articulo) ON DELETE CASCADE " +
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
                        "Cod_Articulo INTEGER PRIMARY KEY AUTOINCREMENT," +
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
                        "Instants text NOT NULL," +
                        "PRIMARY KEY (Cod_Articulo, Precio, Fecha)," +
                        "FOREIGN KEY (Cod_Articulo) REFERENCES Articulos (Cod_Articulo) ON DELETE CASCADE" +
                        ")";

        String queryTipoPago =
                "CREATE TABLE IF NOT EXISTS Tipo_Pago (" +
                        "Cod_Tipo_Pago INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Nombre_Tipo_Pago text NOT NULL," +
                        "Descripcion_Tipo_Pago" +
                        ")";

        String queryIntereses =
                "CREATE TABLE IF NOT EXISTS Intereses_Articulos (" +
                        "Cod_Cliente text," +
                        "Cod_Articulo INTEGER," +
                        "PRIMARY KEY (Cod_Cliente, Cod_Articulo)," +
                        "FOREIGN KEY (Cod_Cliente) REFERENCES Cliente (DNI) ON DELETE CASCADE," +
                        "FOREIGN KEY (Cod_Articulo) REFERENCES Articulos (Cod_Articulo) ON DELETE CASCADE" +
                        ")";

        String queryPresentacion =
                "CREATE TABLE IF NOT EXISTS Presentacion (" +
                        "Cod_Presentacion INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Cod_Cliente text NOT NULL," +
                        "Fecha text NOT NULL," +
                        "Direccion text NOT NULL," +
                        "Observaciones text," +
                        "Venta INTEGER NOT NULL," +
                        "FOREIGN KEY (Cod_Cliente) REFERENCES Cliente (DNI) ON DELETE CASCADE" +
                        ")";

        String queryPuestaEnMarcha =
                "CREATE TABLE IF NOT EXISTS PuestaMarcha (" +
                        "Cod_Puesta_Marcha INTEGER PRIMARY KEY AUTOINCREMENT," +
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
                        "Cod_Accion_Especial INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Nombre_Accion_Especial text NOT NULL," +
                        "Fecha text NOT NULL," +
                        "Tipo_Evento INTEGER NOT NULL," +
                        "Direccion text NOT NULL," +
                        "Observaciones text," +
                        "FOREIGN KEY (Tipo_Evento) REFERENCES Tipo_Evento (Cod_Tipo_Evento) ON DELETE CASCADE" +
                        ")";

        String queryTipoEvento =
                "CREATE TABLE IF NOT EXISTS Tipo_Evento (" +
                        "Cod_Tipo_evento INTEGER PRIMARY KEY AUTOINCREMENT," +
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
                        "Cod_Experiencia INTEGER PRIMARY KEY AUTOINCREMENT," +
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
