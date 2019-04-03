import database.HooverDataBase;

import java.time.LocalDate;

public class mainTest {
    public static void main(String[] Args) {
        HooverDataBase db = new HooverDataBase("Pedro");
/*
        //Comentados porque ya están insertados

        //Clientes
        LocalDate date = LocalDate.of(1992, 2, 17);
        LocalDate date2 = LocalDate.of(1995, 10, 15);
        LocalDate date3 = LocalDate.of(1997, 5, 4);
        db.insertClient("79070458T", "Pedro", "Galindo", "pedrogalindotrabajo@hotmail.com", "622941732", date, "Mi calle", "Buen cliente", "Hombre", false);
        db.insertClient("42240025L", "Angie", "Ruiz Ramos", "angie.ruiz@hotmail.com", "639895040", date, "Su calle", "Mala clienta", "Mujer", true);
        db.insertClient("78548758R", "Daniel", "De la Torre", "daniel@hotmail.com", "656542198", date2, "La calle", "A veces compra, suele llegar a las 6", "Hombre", false);
        db.insertClient("78421589O", "Pablo", "Perez Perez", "pablo@hotmail.com", "652145785", date3, "La Street", "Suel estar con los cascos y no escucha el timbre", "Hombre", false);

        //Productos
        db.insertProduct("Kobold Maxi4000", "La mejor del mercado", "Aspiradora", "C:/TuMadre");
        db.insertProduct("Jabón Maxi Milenario", "El que deja todas las manchas", "Otros", "C:/TuPadre");
        db.insertProduct("Boquilla Antiacaros", "", "Otros", "C:/rutita");
        db.insertProduct("Kobold Maxi3000", "La segunda mejor", "Aspiradora", "C:/ruta");

        //Tipos de Pago
        db.insertTipoPago("Tarjeta", "Solo las de crédito aceptadas!");
        db.insertTipoPago("Efectivo", "Euros");
        db.insertTipoPago("Paypal", "Cuidado con la comisión");

        //Compra
        db.insertCompra("PK2501255452", "79070458T", LocalDate.now(), "Dudoso pero la llevo", 1);
        db.insertCompra("PK25012584228", "42240025L", LocalDate.now(), "Dudoso pero la llevo", 1);
        db.insertCompra("PK25545225457", "78548758R", LocalDate.now(), "Muy contento", 2);
        db.insertCompra("PK25451425456", "78421589O", LocalDate.now(), "Sólo el módulo con el jabon, ya tenia aspiradora", 3);

        //Detalle de compra
        db.insertDetalleCompra("PK25545225457", 3, 1);
        db.insertDetalleCompra("PK25545225457", 4, 1);
        db.insertDetalleCompra("PK25545225457", 2, 5);


        db.insertDetalleCompra("PK25451425456", 3, 1);
        db.insertDetalleCompra("PK25451425456", 2, 4);


        db.insertDetalleCompra("PK2501255452", 4, 3);

        db.insertDetalleCompra("PK25012584228", 1, 2);
        db.insertDetalleCompra("PK25012584228", 2, 10);

        db.insertDetalleCompra("PK2501255452", 1, 1);
        db.insertDetalleCompra("PK2501255452", 2, 3);


        //Intereses
        db.insertInteresCliente("79070458T", 1, "Está muy interesado, realmente lo quiere.", false);
        db.insertInteresCliente("78421589O", 2, "BLA BLA BLA", false);
        db.insertInteresCliente("78421589O", 1, "BLA BLA BLA", false);
        db.insertInteresCliente("78548758R", 4, "BLA BLA BLA", false);

        //Histórico de precios
        db.insertPrecio(1, 2500.10f, LocalDate.now());
        db.insertPrecio(2, 7.90f, LocalDate.now());
        db.insertPrecio(1, 200.00f, LocalDate.now());
        db.insertPrecio(1, 1799.99f, LocalDate.now());

        //Presentacion
        db.insertPresentacion("79070458T", LocalDate.now(), "Calle Paco Calzadilla", "Presentacion mala, el perro no paraba de ladrar", false);

        //Puesta en Marcha
        db.insertPuestaEnMarcha("79070458T", LocalDate.now(), "Todo en orden. La aspiradora funciona sin problemas, y el cliente contento");

        //Tipo Evento
        db.insertTipoEvento("Conferencia");

        //Acciones Especiales
        db.insertAccionesEspeciales("Conferencia Castilla la Mancha", LocalDate.now(), 1, "La Mancha", "No cundio");

        //Acciones Especiales Clientes
        db.insertAccionesEspecialesClientes(1, "79070458T", "Se le regalo una mochila", false, true);
        db.insertAccionesEspecialesClientes(1, "42240025L","" ,false, false);

        //Experiencias
        db.insertExperiencias("Casa Pepe", LocalDate.now(), "Entretenida, atención especial a Manolo que aunque no es cliente, se le puede hacer presentacion");

        //Clientes-Experiencias
        db.insertExperienciasClientes(1, "79070458T", false);
        db.insertExperienciasClientes(1, "42240025L", false);

*/
/*
        //PRUEBAS DE BORRADO

        //Cliente
        db.deleteCliente("79070458T");

        //Compra
        db.deleteCompra("PK25012584228");

        //Detalle compra
        db.deleteDetalleCompra("PK25012584228", 2);

        //Articulo
        db.deleteArticulo(2);

        //Tipo de pago
        db.deleteTipoPago(1);

        //Historico de precios
        db.deletePrecio(1);

        //Intereses
        db.deleteInteres("79070458T", 1);

        //Presentacion
        db.deletePresentacion(1);

        //Puesta en Marcha
        db.deletePuestaMarcha(1);

        //Acciones Especiales - Clientes
        //db.deleteAccionesEspecialesClientes(1, "79070458T");

        //Acciones Especiales
        db.deleteAccionesEspeciales(1);

        //Tipo Evento
        db.deleteTipoEvento(1);

        //Cliente - Experiencias
        db.deleteExperienciasCliente(1, "79070458T");

        //Experiencias
        db.deleteExperiencia(1);

*/




    }
}
