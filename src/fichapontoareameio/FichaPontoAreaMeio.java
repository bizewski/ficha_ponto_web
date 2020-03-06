package fichapontoareameio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import pacote.EgertonUtil;
import ws.ApuracaoPontoFlexBuscaCalculosPorColaboradorIn;
import ws.ApuracaoPontoFlexBuscaCalculosPorColaboradorOut;
import ws.ApuracaoPontoFlexBuscaCalculosPorColaboradorOutCalculos;
import ws.ApuracaoPontoFlexapuracaoPontoIn;
import ws.ApuracaoPontoFlexapuracaoPontoOut;
import ws.ApuracaoPontoFlexapuracaoPontoOutTApuracaoPonto;
import ws.ApuracaoPontoFlexapuracaoPontoOutTColaborador;
import ws.ApuracaoPontoFlexapuracaoPontoOutTEmpregador;
import ws.ApuracaoPontoFlexapuracaoPontoOutTHorarios;
import ws.ApuracaoPontoFlexapuracaoPontoOutTTotalSituacoes;

public class FichaPontoAreaMeio {

    String us;
    String pw;
    ApuracaoPontoFlexBuscaCalculosPorColaboradorOut retornoCalculo;
    ApuracaoPontoFlexBuscaCalculosPorColaboradorIn parametrosCalculos;
    ApuracaoPontoFlexapuracaoPontoOut retornoApuracao;
    ApuracaoPontoFlexapuracaoPontoIn parametrosApuracao;
    int codigoCalculo;
    String dataInicio;
    String dataFim;
    int matricula;
    String empregador;
    String cnpj;
    String endereco;
    String cidade;
    String empregado;
    String categoria;
    String cargo;
    String admissao;
    String ctps;
    String centroCusto;
    String superior;
    String pis;
    String codHorario;
    String horarios;
    String data;
    String semana;
    String codHora;
    String marcacoes;
    String faltas;
    String excedente;
    String atrasos;
    String he50;
    String horasExcedentes;
    String tFaltas;
    String he100;
    String tAtrasos;
    String adcNoturno;
    String he50Not;
    String totalEmFolha;
    String he100Not;
    String totalBancoHoras;
    String totalAcumulado;
    String html;
    EgertonUtil e = new EgertonUtil();
    String userDB;
    String senhaDB;
    ArrayList<ArrayList<String>> retorno;
    ArrayList<ArrayList<String>> codCcu;
    String codigoCcu;
    
    public FichaPontoAreaMeio(String a1, String a2, String a3) {
        credenciais();
        credenciaisDB();
        matricula = Integer.parseInt(a1);
        dataInicio = a2;
        dataFim = a3;
        
        
        pesquisaCalculo();
        parametrosApuracao = new ApuracaoPontoFlexapuracaoPontoIn();
        parametrosApuracao.setNumEmp(new JAXBElement(new QName("numEmp"), Integer.class, 1));
        parametrosApuracao.setTipCol(new JAXBElement(new QName("tipCol"), Integer.class, 1));
        parametrosApuracao.setNumCad(new JAXBElement(new QName("numCad"), Integer.class, matricula));
        parametrosApuracao.setCodCal(new JAXBElement(new QName("codCal"), Integer.class, codigoCalculo));

        retornoApuracao = apuracaoPonto(us, pw, 0, parametrosApuracao);

        System.out.println("Erro: " + retornoApuracao.getErroExecucao().getValue());
      
        
        pesquisaCodCcu();
        empregador();
        empregado();
        totais();
        htmlCompleto();

    }

    public void htmlCompleto() {
        html = lerArquivoString("div.html");
        html = html.replace("@@@EMPREGADOR@@@", empregador);
        html = html.replace("@@@CNPJ@@@", cnpj);
        html = html.replace("@@@ENDERECO@@@", endereco);
        html = html.replace("@@@CIDADE@@@", cidade);
        html = html.replace("@@@EMPREGADO@@@", empregado);
        html = html.replace("@@@CATEGORIA@@@", categoria);
        html = html.replace("@@@CARGO@@@", cargo);
        html = html.replace("@@@ADMISSAO@@@", admissao);
        html = html.replace("@@@CTPS@@@", ctps);
        html = html.replace("@@@CENTRO_CUSTO@@@", centroCusto);
        html = html.replace("@@@SUPERVISOR@@@", superior);
        html = html.replace("@@@PIS@@@", pis);
        html = html.replace("@@@HE_50@@@", he50);
        html = html.replace("@@@HORAS_EXCEDENTES@@@", horasExcedentes);
        html = html.replace("@@@T_FALTAS@@@", tFaltas);
        html = html.replace("@@@HE_100@@@", he100);
        html = html.replace("@@@T_ATRASOS@@@", tAtrasos);
        html = html.replace("@@@ADC_NOTURNO@@@", adcNoturno);
        html = html.replace("@@@HE_50_NOT@@@", he50Not);
        html = html.replace("@@@TOTAL_EM_FOLHA@@@", totalEmFolha);
        html = html.replace("@@@HE_100_NOT@@@", he100Not);
        html = html.replace("@@@TOTAL_BANCO_HORAS@@@", totalBancoHoras);
        html = html.replace("@@@ACUMULADO@@@", totalAcumulado);

        html = html.replace("@@@LOOP_HORARIOS@@@", stringHorarios());
        html = html.replace("@@@LOOP_MARCACOES@@@", stringMarcacoes());
      //  System.out.println(html);
    }

    public void empregador() {
        for (ApuracaoPontoFlexapuracaoPontoOutTEmpregador c : retornoApuracao.getTEmpregador()) {
            empregador = c.getRazSoc().getValue();
            cnpj = c.getNumEmp().getValue().toString();
            endereco = c.getEndRua().getValue().toString();
            cidade = c.getNomCid().getValue() + " " + c.getEstCid().getValue();
        }
    }

    public void empregado() {
        for (ApuracaoPontoFlexapuracaoPontoOutTColaborador c : retornoApuracao.getTColaborador()) {
            empregado = c.getNumCad().getValue().toString() + " " + c.getNomFun().getValue();
            categoria = c.getTipSal().getValue();
            cargo = c.getTitRed().getValue();
            admissao = c.getDatAdm().getValue();
            ctps = c.getNumCtp().getValue().toString() + " - " + c.getSerCtp().getValue().toString();
            centroCusto = codigoCcu.toString(); 
            superior = c.getSupPro().getValue().toString();
            pis = c.getNumPis().getValue().toString();
        }
    }

    public void totais() {
        for (ApuracaoPontoFlexapuracaoPontoOutTTotalSituacoes c : retornoApuracao.getTTotalSituacoes()) {
            he50 = c.getTotHE50().getValue();
            horasExcedentes = c.getTotExc().getValue();
            tFaltas = c.getTotFal().getValue();
            he100 = c.getTotHE100().getValue();
            tAtrasos = c.getTotAtr().getValue();
            adcNoturno = c.getTotNot().getValue();
            he50Not = c.getTotHE50Not().getValue();
            totalEmFolha = c.getTotPag().getValue();
            he100Not = c.getTotHE100Not().getValue();
            totalBancoHoras = c.getSalBco().getValue();
            //totalBancoHoras = c.getSalAnt().getValue() + c.getTotPag().getValue();
            totalAcumulado = c.getSalAnt().getValue();
           System.out.println("totalBancoHoras: "+totalBancoHoras);
           System.out.println("totalEmFolha: "+totalEmFolha);
            
            
        }
    }

    public String stringHorarios() {
        String htmlHorarios = lerArquivoString("horarios.html");
        String retorno = "";
        for (ApuracaoPontoFlexapuracaoPontoOutTHorarios h : retornoApuracao.getTHorarios()) {
            retorno += htmlHorarios;
            retorno = retorno.replace("@@@COD_HORARIO@@@", h.getCodHor().getValue().toString());
            retorno = retorno.replace("@@@HORARIOS@@@", h.getHorMar().getValue());
        }
        return retorno;
    }

    public String stringMarcacoes() {
        pesquisaTodasObs();
        String htmlMarcacoes = lerArquivoString("marcacoes.html");
        String retorno = "";
        for (ApuracaoPontoFlexapuracaoPontoOutTApuracaoPonto a : retornoApuracao.getTApuracaoPonto()) {
            retorno += htmlMarcacoes;
            retorno = retorno.replace("@@@DATA@@@", a.getDatApu().getValue().toString());
            retorno = retorno.replace("@@@SEMANA@@@", a.getDiaSem().getValue().toString());
            retorno = retorno.replace("@@@COD_HORA@@@", a.getHorDat().getValue().toString());
            retorno = retorno.replace("@@@MARCACOES@@@", a.getHorMar().getValue().toString());
            retorno = retorno.replace("@@@FALTAS@@@", a.getTotFal().getValue().toString().replace("00:00", ""));
            retorno = retorno.replace("@@@EXCEDENTE@@@", a.getTotExc().getValue().toString().replace("00:00", ""));
            retorno = retorno.replace("@@@ATRASOS@@@", a.getTotAtr().getValue().toString().replace("00:00", ""));
            retorno = retorno.replace("@@@OBS@@@", pesquisaObs(a.getDatApu().getValue().toString()).get(2));
            retorno = retorno.replace("@@@ID@@@", pesquisaObs(a.getDatApu().getValue().toString()).get(0));
        }
        return retorno;
    }

    public void pesquisaTodasObs() {
//        retorno = new ArrayList<>();
        String sql = "USE BANCO_GERAL_SISTEMAS\n"
                + "SELECT\n"
                + "ID,DATA,OBS,ALTERADO\n"
                + "FROM OBS_FICHA_PONTO_WEB WITH (NOLOCK)\n"
                + "WHERE MATRICULA = '" + matricula + "'\n"
                + "AND "
                + "(DATA LIKE '%" + dataInicio.substring(3, dataInicio.length()) + "' OR DATA LIKE '%" + dataFim.substring(3, dataFim.length()) + "')"
                + " ORDER BY 1 DESC";
        retorno = e.selectSQL(sql, 4, "172.28.2.21", "1433", "", userDB, senhaDB, "sqlserver");
//        System.out.println(dataInicio.substring(3, dataInicio.length()));
    }

    public ArrayList<String> pesquisaObs(String dt) {
        ArrayList<ArrayList<String>> retorno2;

        for (ArrayList<String> x : retorno) {
            if (dt.equals(x.get(1))) {
//                System.out.println(x);
                return x;
            }
        }
//        System.out.println(retorno);
//        if (retorno.size() > 0) {
////            ArrayList<String> r = retorno.get(0);
////            System.out.println(retorno.get(1));
//            if (dt.equals(retorno.get(0).get(1))) {
//                return retorno;
//            }
//        }

//        retorno = new ArrayList<>();
        ArrayList<String> r = new ArrayList();
        for (int i = 0; i < 4; i++) {
            r.add("");
        }
//        retorno.add(r);
//        System.out.println(r);
        return r;
    }

    public String pesquisaCodCcu(){
    
        String sql = "USE Senior\n"
                +"SELECT\n"
                +"CODCCU \n"
                +"FROM R034FUN \n"
                +"WHERE NUMCAD = " + matricula;
             
        codCcu = e.selectSQL(sql, 1, "172.28.2.150", "1433", "", userDB, senhaDB, "sqlserver"); 

        codigoCcu = String.valueOf(codCcu.get(0));
       
        return codigoCcu; 
        
    }

    public void pesquisaCalculo() {
        parametrosCalculos = new ApuracaoPontoFlexBuscaCalculosPorColaboradorIn();
        parametrosCalculos.setNumEmp(new JAXBElement(new QName("numEmp"), Integer.class, 1));
        parametrosCalculos.setTipCol(new JAXBElement(new QName("tipCol"), Integer.class, 1));
        parametrosCalculos.setNumCad(new JAXBElement(new QName("numCad"), Integer.class, matricula));
        parametrosCalculos.setFiltroIniCmp(new JAXBElement(new QName("filtroIniCmp"), String.class, dataInicio));
        parametrosCalculos.setFiltroFimCmp(new JAXBElement(new QName("filtroFimCmp"), String.class, dataFim));
        parametrosCalculos.setRetornaCalcCancelado(new JAXBElement(new QName("retornaCalcCancelado"), Integer.class, 1));
        parametrosCalculos.setRetornaCalcInicializado(new JAXBElement(new QName("retornaCalcInicializado"), Integer.class, 1));
        parametrosCalculos.setRetornaCalcParcial(new JAXBElement(new QName("retornaCalcParcial"), Integer.class, 1));
        parametrosCalculos.setRetornaCalcTotal(new JAXBElement(new QName("retornaCalcTotal"), Integer.class, 1));
        retornoCalculo = buscaCalculosPorColaborador(us, pw, 0, parametrosCalculos);
        for (ApuracaoPontoFlexBuscaCalculosPorColaboradorOutCalculos x : retornoCalculo.getCalculos()) {
//            System.out.println(">" + x.getCodCal().getValue());
            codigoCalculo = x.getCodCal().getValue();
        }
    }

    public void credenciais() {
        us = "";
        us += (char) 119;
        us += (char) 101;
        us += (char) 98;
        us += (char) 112;
        us += (char) 111;
        us += (char) 114;
        us += (char) 116;
        us += (char) 97;
        us += (char) 108;
        pw = "";
        pw += (char) 119;
        pw += (char) 101;
        pw += (char) 98;
        pw += (char) 97;
        pw += (char) 49;
        pw += (char) 115;
        pw += (char) 50;
        pw += (char) 100;
        pw += (char) 51;
    }

    public String lerArquivoString(String caminhoArquivo) {
        String linhaArquivo;
        File arquivo;
        FileReader fileReader;
        BufferedReader bufferedReader;
        String conteudo = "";
        arquivo = new File(caminhoArquivo);
        linhaArquivo = "";
        try {
            fileReader = new FileReader(arquivo);
            bufferedReader = new BufferedReader(fileReader);
            while ((linhaArquivo = bufferedReader.readLine()) != null) {
                conteudo += linhaArquivo;
            }
            fileReader.close();
            bufferedReader.close();
        } catch (IOException e) {
//            System.out.println("lerArquivo():\n" + e);
        }
        return conteudo;
    }

//    private static ApuracaoPontoFlexBuscaCalculosPorColaboradorOut buscaCalculosPorColaborador(java.lang.String user, java.lang.String password, int encryption, ws.ApuracaoPontoFlexBuscaCalculosPorColaboradorIn parameters) {
//        ws.G5SeniorServices service = new ws.G5SeniorServices();
//        ws.RondaSynccomSeniorG5RhHrApuracaoPontoFlex port = service.getRondaSynccomSeniorG5RhHrApuracaoPontoFlexPort();
//        return port.buscaCalculosPorColaborador(user, password, encryption, parameters);
//    }
//
//    private static ApuracaoPontoFlexapuracaoPontoOut apuracaoPonto(java.lang.String user, java.lang.String password, int encryption, ws.ApuracaoPontoFlexapuracaoPontoIn parameters) {
//        ws.G5SeniorServices service = new ws.G5SeniorServices();
//        ws.RondaSynccomSeniorG5RhHrApuracaoPontoFlex port = service.getRondaSynccomSeniorG5RhHrApuracaoPontoFlexPort();
//        return port.apuracaoPonto(user, password, encryption, parameters);
//    }

    public void credenciaisDB() {
        userDB = "";
        userDB += (char) 115;
        userDB += (char) 105;
        userDB += (char) 115;
        userDB += (char) 116;
        userDB += (char) 101;
        userDB += (char) 109;
        userDB += (char) 97;
        userDB += (char) 115;
        senhaDB = "";
        senhaDB += (char) 102;
        senhaDB += (char) 108;
        senhaDB += (char) 51;
        senhaDB += (char) 120;
        senhaDB += (char) 115;
        senhaDB += (char) 105;
        senhaDB += (char) 115;
        senhaDB += (char) 116;
        senhaDB += (char) 101;
        senhaDB += (char) 109;
        senhaDB += (char) 97;
        senhaDB += (char) 115;

    }

    public static void main(String[] args) {
        String a1 = args[0];
        String a2 = args[1];
        String a3 = args[2];
        new FichaPontoAreaMeio(a1, a2, a3);
//      new FichaPontoAreaMeio("40167", "26/02/2019", "25/03/2019");
//        new FichaPontoAreaMeio("3660", "26/02/2019", "25/03/2019");

    }

    private static ApuracaoPontoFlexBuscaCalculosPorColaboradorOut buscaCalculosPorColaborador(java.lang.String user, java.lang.String password, int encryption, ws.ApuracaoPontoFlexBuscaCalculosPorColaboradorIn parameters) {
        ws.G5SeniorServices service = new ws.G5SeniorServices();
        ws.RondaSynccomSeniorG5RhHrApuracaoPontoFlex port = service.getRondaSynccomSeniorG5RhHrApuracaoPontoFlexPort();
        return port.buscaCalculosPorColaborador(user, password, encryption, parameters);
    }

    private static ApuracaoPontoFlexapuracaoPontoOut apuracaoPonto(java.lang.String user, java.lang.String password, int encryption, ws.ApuracaoPontoFlexapuracaoPontoIn parameters) {
        ws.G5SeniorServices service = new ws.G5SeniorServices();
        ws.RondaSynccomSeniorG5RhHrApuracaoPontoFlex port = service.getRondaSynccomSeniorG5RhHrApuracaoPontoFlexPort();
        return port.apuracaoPonto(user, password, encryption, parameters);
    }

}
