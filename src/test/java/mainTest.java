import database.HooverDataBase;

import java.time.LocalDate;

public class mainTest {
    public static void main(String Args[]) {
        HooverDataBase db = new HooverDataBase("Pedro");
        /*
        //Comentados porque ya están insertados

        //Clientes
        LocalDate date = LocalDate.of(1992, 02, 17);
        db.insertClient("79070458T", "Pedro", "Galindo", "pedrogalindotrabajo@hotmail.com", "622941732", date, "Mi calle", "Buen cliente", "Hombre", false);
        db.insertClient("42240025L", "Angie", "Ruiz Ramos", "angie.ruiz@hotmail.com", "639895040", date, "Su calle", "Mala clienta", "Mujer", true);

        //Productos
        db.insertProduct("Kobold Maxi4000", "La mejor del mercado", "Aspiradora", "C:/TuMadre");
        db.insertProduct("Jabón Maxi Milenario", "El que deja todas las manchas", "Otros", "C:/TuPadre");

        //Tipos de Pago
        db.insertTipoPago("Tarjeta", "Solo las de crédito aceptadas!");

        //Compra
        db.insertCompra("PK2501255452", "79070458T", LocalDate.now(), "Dudoso pero la llevo", 1);
        db.insertCompra("PK25012584228", "42240025L", LocalDate.now(), "Dudoso pero la llevo", 1);

        //Detalle de compra
        db.insertDetalleCompra("PK2501255452", 1, 1);
        db.insertDetalleCompra("PK2501255452", 2, 3);

        db.insertDetalleCompra("PK25012584228", 1, 2);
        db.insertDetalleCompra("PK25012584228", 2, 10);

        //Intereses
        db.insertInteresCliente("79070458T", 1, "Está muy interesado, realmente lo quiere.", false);

        //Histórico de precios
        db.insertPrecio(1, 25.10f, LocalDate.now());

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
    }
}
