package br.com.gdelon.lib.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

import java.util.UUID;


/**
 * Created by geandelon on 08/06/2017.
 */
public class Rastreamento extends BaseModel implements Parcelable {

    public static final String TABELA = "GPS_RASTREAMENTO";

    private String codigoUsuario;

    private String hash;

    private Double latitude;

    private Double longitude;

    private Double velocidade;

    private Double acuracia;

    private DateTime dataHoraCaptura;

    private DateTime dataHoraEnvio;

    private float distancia;

    private String informacoesAdicionais;

    public Rastreamento() {
        this.id = 0;
    }

    public Rastreamento(int id) {
        this();
        this.id = id;
    }

    public Rastreamento(Location location, String codigoUsuario) {
        this();
        this.setCodigoUsuario(codigoUsuario);
        this.setHash(UUID.randomUUID().toString());
        this.setLatitude(location.getLatitude());
        this.setLongitude(location.getLongitude());
        this.setVelocidade(Double.parseDouble(Float.toString(location.getSpeed())));
        this.setAcuracia(Double.parseDouble(Float.toString(location.getAccuracy())));
        this.setDataHoraCaptura(new DateTime());
    }

    public Rastreamento(Location location, String codigoUsuario, String jsonInformacoesAdicionais) {
        this();
        this.setCodigoUsuario(codigoUsuario);
        this.setHash(UUID.randomUUID().toString());
        this.setLatitude(location.getLatitude());
        this.setLongitude(location.getLongitude());
        this.setVelocidade(Double.parseDouble(Float.toString(location.getSpeed())));
        this.setAcuracia(Double.parseDouble(Float.toString(location.getAccuracy())));
        this.setDataHoraCaptura(new DateTime());
        this.setInformacoesAdicionais(jsonInformacoesAdicionais);
    }

    public Rastreamento(Parcel in) {
        this.codigoUsuario = in.readString();
        this.hash = in.readString();
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
        this.velocidade = (Double) in.readValue(Double.class.getClassLoader());
        this.acuracia = (Double) in.readValue(Double.class.getClassLoader());
        this.dataHoraCaptura = (DateTime) in.readSerializable();
        this.dataHoraEnvio = (DateTime) in.readSerializable();
        this.distancia = in.readFloat();
        this.informacoesAdicionais = in.readString();
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

    public Double getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(Double velocidade) {
        this.velocidade = velocidade;
    }

    public Double getAcuracia() {
        return acuracia;
    }

    public void setAcuracia(Double acuracia) {
        this.acuracia = acuracia;
    }

    public DateTime getDataHoraCaptura() {
        return dataHoraCaptura;
    }

    public void setDataHoraCaptura(DateTime dataHoraCaptura) {
        this.dataHoraCaptura = dataHoraCaptura;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    public String getInformacoesAdicionais() {
        return informacoesAdicionais;
    }

    public void setInformacoesAdicionais(String informacoesAdicionais) {
        this.informacoesAdicionais = informacoesAdicionais;
    }

    public DateTime getDataHoraEnvio() {
        return dataHoraEnvio;
    }

    public void setDataHoraEnvio(DateTime dataHoraEnvio) {
        this.dataHoraEnvio = dataHoraEnvio;
    }

    public Rastreamento calculaDistancia(Rastreamento rastreamento){

        Location newLocation = new Location("newlocation");
        newLocation.setLatitude(this.getLatitude());
        newLocation.setLongitude(this.getLongitude());
        newLocation.setTime(this.getDataHoraCaptura().getMillis());

        Location oldLocation  = new Location("oldlocation");
        oldLocation.setLatitude(rastreamento.getLatitude());
        oldLocation.setLongitude(rastreamento.getLongitude());
        oldLocation.setTime(rastreamento.getDataHoraCaptura().getMillis());
        this.setDistancia(((oldLocation.distanceTo(newLocation))));
        return this;
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
        dest.writeValue(this.velocidade);
        dest.writeValue(this.acuracia);
        dest.writeSerializable(this.dataHoraCaptura);
        dest.writeSerializable(this.dataHoraEnvio);
        dest.writeFloat(this.distancia);
        dest.writeString(this.informacoesAdicionais);
    }

    public static final Creator<Rastreamento> CREATOR = new Creator<Rastreamento>() {
        @Override
        public Rastreamento createFromParcel(Parcel source) {
            return new Rastreamento(source);
        }

        @Override
        public Rastreamento[] newArray(int size) {
            return new Rastreamento[size];
        }
    };
}
