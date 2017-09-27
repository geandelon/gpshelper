package br.com.gdelon.lib.arquitetura.utilitario;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.text.format.DateFormat;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UtilData {

    public static final String FORMATO_DATA_HORA_BRASIL = "dd/MM/yyyy HH:mm:ss";
    public static final String FORMATO_DATA_BRASIL = "dd/MM/yyyy";
    public static final String FORMATO_DATA_PASTA_BRASIL = "dd_MM_yyyy";
    public static final String FORMATO_DATA_HORA_USA = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMATO_DATA_HORA_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
    public static final String FORMATO_DATA_USA = "yyyy-MM-dd";
    public static final String FORMATO_DATA_HORA_BRASIL_ARQUIVO = "dd_MM_yyyy_HH_mm_ss";

    @SuppressLint("SimpleDateFormat")
    private static java.text.DateFormat dateFormat = new SimpleDateFormat(FORMATO_DATA_HORA_BRASIL);

    @SuppressLint("SimpleDateFormat")
    public static Date formatarDataString(String s) {
        Date date;
        java.text.DateFormat dateFormatServico = new SimpleDateFormat(FORMATO_DATA_HORA_USA);
        try {
            String as[] = s.split(" ");
            String as1[] = as[0].split("-");
            date = dateFormatServico.parse((new StringBuilder(String.valueOf(as1[1]))).append("-").append(as1[2]).append("-").append(as1[0]).append(" ").append(as[1]).toString());
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        return date;
    }

    public static String converterHora(int segundos) {
        Calendar cal = Calendar.getInstance();
        cal.set(1900, 0, 1, 0, 0, segundos);
        return String.valueOf(DateFormat.format("kk:mm:ss", cal));
    }

    /**
     * Retorna a data e hora no padrão dd/MM/yyyy HH:mm:ss
     *
     * @return
     */
    public static String getDataHora() {
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static String getDataHora(Date dt) {
        return dateFormat.format(dt.getTime());
    }

    public static String getDataHora(int segundos) {
        Calendar cal = Calendar.getInstance();
        cal.set(1900, 0, 1, 0, 0, segundos);
        return dateFormat.format(cal.getTime());
    }

    public static Date getData(String str) throws ParseException {
        return dateFormat.parse(str);
    }

    public static DateTime getDateTimeHoraDeterminada(String horario) {

        DateTime dataAgoraSistema = new DateTime();

        //formato aceito 00:00:00:00
        String[] tempoSeparado = horario.split(":");


        DateTime dataResult = null;

        // 00:00:00:00
        if (tempoSeparado.length == 4) {
            dataResult = new DateTime(
                    dataAgoraSistema.getYear(),
                    dataAgoraSistema.getMonthOfYear(),
                    dataAgoraSistema.getDayOfMonth(),
                    Integer.valueOf(tempoSeparado[0]), Integer.valueOf(tempoSeparado[1]), Integer.valueOf(tempoSeparado[2]), Integer.valueOf(tempoSeparado[3]));

        }
        // 00:00:00
        if (tempoSeparado.length == 3) {
            dataResult = new DateTime(
                    dataAgoraSistema.getYear(),
                    dataAgoraSistema.getMonthOfYear(),
                    dataAgoraSistema.getDayOfMonth(),
                    Integer.valueOf(tempoSeparado[0]), Integer.valueOf(tempoSeparado[1]), Integer.valueOf(tempoSeparado[2]), 0);

        }
        // 00:00
        if (tempoSeparado.length == 2) {
            dataResult = new DateTime(
                    dataAgoraSistema.getYear(),
                    dataAgoraSistema.getMonthOfYear(),
                    dataAgoraSistema.getDayOfMonth(),
                    Integer.valueOf(tempoSeparado[0]), Integer.valueOf(tempoSeparado[1]), 0, 0);

        }
        // 00
        if (tempoSeparado.length == 1) {
            dataResult = new DateTime(
                    dataAgoraSistema.getYear(),
                    dataAgoraSistema.getMonthOfYear(),
                    dataAgoraSistema.getDayOfMonth(),
                    Integer.valueOf(tempoSeparado[0]), 0, 0, 0);

        }

        return dataResult;
    }

    public static String getDataSqlite(Object data) {
        if (data != null) {
            DateTimeFormatter dtf;
            if (data instanceof DateTime) {
                dtf = DateTimeFormat.forPattern(FORMATO_DATA_HORA_ISO_8601);
                return dtf.print((DateTime) data);
            } else if (data instanceof LocalDate) {
                dtf = DateTimeFormat.forPattern(FORMATO_DATA_USA);
                return dtf.print((LocalDate) data);
            }
        }
        return null;
    }

    public static Long getTimeZoneOffset(DateTime dateTime) {
        if (dateTime != null) {
            return TimeUnit.MILLISECONDS.toHours(dateTime.getZone().getOffset(dateTime.getMillis()));
        }
        return null;
    }

    public static Object getDateTime(String dataHora) {
        if (TextUtils.isEmpty(dataHora)) {
            return null;
        }

        DateTimeFormatter formatter;
        if (dataHora.length() == 10) {
            formatter = DateTimeFormat.forPattern(FORMATO_DATA_USA);
            return formatter.parseLocalDate(dataHora);
        } else {
            formatter = DateTimeFormat.forPattern(FORMATO_DATA_HORA_ISO_8601);
            return formatter.parseDateTime(dataHora);
        }
    }
}
