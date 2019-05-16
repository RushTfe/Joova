package model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class NuevoProductoModel {
    private IntegerProperty codArticulo;
    private StringProperty direccionImagen;
    private StringProperty nombreProducto;
    private StringProperty tipoProducto;
    private StringProperty descripcionProducto;
    private DoubleProperty precioProducto;
    private BooleanProperty listoParaInsertar;
    private BooleanProperty aModificar;
    private ObjectProperty<ImageView> imagen;

    public NuevoProductoModel() {
        codArticulo = new SimpleIntegerProperty(this, "codArticulo");
        direccionImagen = new SimpleStringProperty(this, "direccionImagen");
        nombreProducto = new SimpleStringProperty(this, "nombreProducto");
        tipoProducto = new SimpleStringProperty(this, "tipoProducto");
        descripcionProducto = new SimpleStringProperty(this, "descripcionProducto");
        precioProducto = new SimpleDoubleProperty(this, "precioProducto");
        listoParaInsertar = new SimpleBooleanProperty(this, "listoParaInsertar");
        aModificar = new SimpleBooleanProperty(this, "aModificar", false);
        imagen = new SimpleObjectProperty<>(this, "imagen");
    }
    public  NuevoProductoModel(ImageView img) {
        this();
        imagen.setValue(img);
    }

    public ImageView getImagen() {
        return imagen.get();
    }

    public ObjectProperty<ImageView> imagenProperty() {
        return imagen;
    }

    public void setImagen(ImageView imagen) {
        this.imagen.set(imagen);
    }

    public boolean isaModificar() {
        return this.aModificar.get();
    }

    public BooleanProperty aModificarProperty() {
        return this.aModificar;
    }

    public void setaModificar(boolean aModificar) {
        this.aModificar.set(aModificar);
    }

    public boolean isListoParaInsertar() {
        return this.listoParaInsertar.get();
    }

    public BooleanProperty listoParaInsertarProperty() {
        return this.listoParaInsertar;
    }

    public void setListoParaInsertar(boolean listoParaInsertar) {
        this.listoParaInsertar.set(listoParaInsertar);
    }

    public int getCodArticulo() {
        return this.codArticulo.get();
    }

    public IntegerProperty codArticuloProperty() {
        return this.codArticulo;
    }

    public void setCodArticulo(int codArticulo) {
        this.codArticulo.set(codArticulo);
    }

    public String getDescripcionProducto() {
        return (String)this.descripcionProducto.get();
    }

    public StringProperty descripcionProductoProperty() {
        return this.descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto.set(descripcionProducto);
    }

    public String getDireccionImagen() {
        return (String)this.direccionImagen.get();
    }

    public StringProperty direccionImagenProperty() {
        return this.direccionImagen;
    }

    public void setDireccionImagen(String direccionImagen) {
        this.direccionImagen.set(direccionImagen);
    }

    public String getNombreProducto() {
        return (String)this.nombreProducto.get();
    }

    public StringProperty nombreProductoProperty() {
        return this.nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto.set(nombreProducto);
    }

    public String getTipoProducto() {
        return (String)this.tipoProducto.get();
    }

    public StringProperty tipoProductoProperty() {
        return this.tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto.set(tipoProducto);
    }

    public double getPrecioProducto() {
        return this.precioProducto.get();
    }

    public DoubleProperty precioProductoProperty() {
        return this.precioProducto;
    }

    public void setPrecioProducto(double precioProducto) {
        this.precioProducto.set(precioProducto);
    }

    public StringProperty precioAsString() {
        StringProperty string = new SimpleStringProperty(this, "string", this.precioProductoProperty().getName());
        string.bind(Bindings.concat(new Object[]{this.precioProducto, "€"}));
        return string;
    }

    public String toString() {
        return this.getNombreProducto();
    }
}
