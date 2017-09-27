package br.com.gdelon.lib.model.transform;

import android.database.Cursor;

import org.joda.time.DateTime;

import br.com.gdelon.lib.arquitetura.persistencia.BaseMap;
import br.com.gdelon.lib.arquitetura.transform.TransformerInterface;
import br.com.gdelon.lib.arquitetura.utilitario.UtilData;
import br.com.gdelon.lib.model.Rastreamento;

/**
 * Created by geandelon on 12/06/2017.
 */

public class RastreamentoTransformer implements TransformerInterface<Rastreamento> {

    @Override
    public Rastreamento toModelo(Cursor cursor) {
        Rastreamento localizacaoRastreamento = new Rastreamento();

        int idColunaId = cursor.getColumnIndex(BaseMap.ID_RASTREAMENTO_MAP);
        if (idColunaId == -1) {
            idColunaId = cursor.getColumnIndex(BaseMap.ID_MAP);
        }

        if (idColunaId != -1) {
            localizacaoRastreamento.setId(cursor.getInt(idColunaId));
        }

        if (cursor.getColumnIndex(BaseMap.CODIGO_USUARIO_MAP) != -1) {
            localizacaoRastreamento.setCodigoUsuario(cursor.getString(cursor.getColumnIndex(BaseMap.CODIGO_USUARIO_MAP)));
        }

        if (cursor.getColumnIndex(BaseMap.HASH_MAP) != -1) {
            localizacaoRastreamento.setHash(cursor.getString(cursor.getColumnIndex(BaseMap.HASH_MAP)));
        }

        if (cursor.getColumnIndex(BaseMap.LATITUDE_MAP) != -1) {
            localizacaoRastreamento.setLatitude(
                    cursor.getDouble(cursor.getColumnIndex(BaseMap.LATITUDE_MAP)));
        }

        if (cursor.getColumnIndex(BaseMap.LONGITUDE_MAP) != -1) {
            localizacaoRastreamento.setLongitude(
                    cursor.getDouble(cursor.getColumnIndex(BaseMap.LONGITUDE_MAP)));
        }

        if (cursor.getColumnIndex(BaseMap.VELOCIDADE_MAP) != -1) {
            localizacaoRastreamento.setVelocidade(
                    cursor.getDouble(cursor.getColumnIndex(BaseMap.VELOCIDADE_MAP)));
        }

        if (cursor.getColumnIndex(BaseMap.ACURACIA_MAP) != -1) {
            localizacaoRastreamento.setAcuracia(
                    cursor.getDouble(cursor.getColumnIndex(BaseMap.ACURACIA_MAP)));
        }

        if (cursor.getColumnIndex(BaseMap.DATA_HORA_CAPTURA_MAP) != -1) {
            localizacaoRastreamento.setDataHoraCaptura(
                    (DateTime) UtilData.getDateTime(cursor.getString(cursor.getColumnIndex(BaseMap.DATA_HORA_CAPTURA_MAP))));
        }

        if (cursor.getColumnIndex(BaseMap.DATA_HORA_ENVIO_MAP) != -1) {
            localizacaoRastreamento.setDataHoraEnvio(
                    (DateTime) UtilData.getDateTime(cursor.getString(cursor.getColumnIndex(BaseMap.DATA_HORA_ENVIO_MAP))));
        }

        if (cursor.getColumnIndex(BaseMap.DISTANCIA_MAP) != -1) {
            localizacaoRastreamento.setDistancia(cursor.getFloat(cursor.getColumnIndex(BaseMap.DISTANCIA_MAP)));
        }
        if (cursor.getColumnIndex(BaseMap.INFORMACOES_ADICIONAIS_MAP) != -1) {
            localizacaoRastreamento.setInformacoesAdicionais(cursor.getString(cursor.getColumnIndex(BaseMap.INFORMACOES_ADICIONAIS_MAP)));
        }

        return localizacaoRastreamento;
    }

}
