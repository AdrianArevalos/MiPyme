package objetos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mundo on 25/10/2016.
 */

public class Listado implements Parcelable {
    private int id_producto;
    private int cantidad;
    private String descripcion;
    private int precio_unitario;

    public Listado(int id_producto, int cantidad, String descripcion, int precio_unitario) {
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.precio_unitario = precio_unitario;
    }

    public Listado(Parcel parcel){
        this.id_producto = parcel.readInt();
        this.cantidad = parcel.readInt();
        this.descripcion = parcel.readString();
        this.precio_unitario = parcel.readInt();
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getPrecio_unitario() { return precio_unitario; }

    public void setPrecio_unitario(int precio_unitario) { this.precio_unitario = precio_unitario; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id_producto);
        dest.writeInt(this.cantidad);
        dest.writeString(this.descripcion);
        dest.writeInt(this.precio_unitario);
    }

    public static final Parcelable.Creator<Listado> CREATOR = new Creator<Listado>() {
        @Override
        public Listado createFromParcel(Parcel source) {
            return new Listado(source);
        }

        @Override
        public Listado[] newArray(int size) {
            return new Listado[size];
        }
    };
}
