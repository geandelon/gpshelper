package br.com.gdelon.lib.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

/**
 * Created by geandelon on 12/06/2017.
 */

public class Evento extends BaseModel implements Parcelable {

    public static final String TABELA = "GPS_EVENTO";

    private String codigoUsuario;

    private String hash;

    private Double latitude;

    private Double longitude;

    private String informacoesAdicionais;

    private DateTime dataHoraCaptura;

    private DateTime dataHoraEnvio;

    private int tipo;

    public Evento() {
        this.id = 0;
    }

    public Evento(int id) {
        this();
        this.id = id;
    }

    public Evento(Parcel in) {
        this.codigoUsuario = in.readString();
        this.hash = in.readString();
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
        this.informacoesAdicionais = in.readString();
        this.dataHoraCaptura = (DateTime) in.readSerializable();
        this.dataHoraEnvio = (DateTime) in.readSerializable();
        this.tipo = in.readInt();
    }

    @Override
    public String getNomeTabela() {
        return TABELA;
    }


    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getInformacoesAdicionais() {
        return informacoesAdicionais;
    }

    public void setInformacoesAdicionais(String informacoesAdicionais) {
        this.informacoesAdicionais = informacoesAdicionais;
    }

    public DateTime getDataHoraCaptura() {
        return dataHoraCaptura;
    }

    public void setDataHoraCaptura(DateTime dataHoraCaptura) {
        this.dataHoraCaptura = dataHoraCaptura;
    }

    public DateTime getDataHoraEnvio() {
        return dataHoraEnvio;
    }

    public void setDataHoraEnvio(DateTime dataHoraEnvio) {
        this.dataHoraEnvio = dataHoraEnvio;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.codigoUsuario);
        dest.writeString(this.hash);
        dest.writeValue(this.latitude);
        dest.writeValue(this.longitude);
        dest.writeString(this.informacoesAdicionais);
        dest.writeSerializable(this.dataHoraCaptura);
        dest.writeSerializable(this.dataHoraEnvio);
        dest.writeInt(this.tipo);
    }

    public static final Creator<Evento> CREATOR = new Creator<Evento>() {
        @Override
        public Evento createFromParcel(Parcel source) {
            return new Evento(source);
        }

        @Override
        public Evento[] newArray(int size) {
            return new Evento[size];
        }
    };
}
