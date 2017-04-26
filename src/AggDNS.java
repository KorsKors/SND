import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.joda.time.DateTime;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by George on 14.04.2017.
 */
public class AggDNS {

    SearchResponse response;

    private String clientkey = null;
    private String data = null;
    private String clientdestkey = null;
    private String client_ports = null;
    private Avg bytesIn;
    private Avg bytesOut;
    private int counter=0;
    private double bytesins = 0;
    private double bytesouts = 0;
    private int countPortMax =0;
    int countOut = 0;
    double countError = 0;
    double counALL=0;


    public AggDNS(SearchResponse response) {
        this.response = response;
    }
    public void go() {

        try (FileWriter writer = new FileWriter("D:\\log.txt", true)) {

            Histogram histogram = response.getAggregations().get("data");
            for (Histogram.Bucket entry0 : histogram.getBuckets()) {
                DateTime data = (DateTime) entry0.getKey();

                Terms clients = entry0.getAggregations().get("client_ip");

                //System.out.println(data);
                //if ( clients!= null) {
                for (Terms.Bucket entry1 : clients.getBuckets()) {
                    clientkey = entry1.getKeyAsString();


                    Terms clientdest = entry1.getAggregations().get("ip");

                    for (Terms.Bucket entry2 : clientdest.getBuckets()) {
                        clientdestkey = entry2.getKeyAsString();
                        Terms client_port = entry2.getAggregations().get("client_port");
                        //if (icpmrequest != null) {
                        for (Terms.Bucket entry3 : client_port.getBuckets()) {
                            client_ports = entry3.getKeyAsString();
                            countOut = (int) entry3.getDocCount();
                            bytesIn = entry3.getAggregations().get("bytes_in");
                            bytesins = bytesIn.getValue();
                            bytesOut = entry3.getAggregations().get("bytes_out");
                            bytesouts = bytesOut.getValue();
                            counALL = counALL + 1;
                            if (bytesins > 75) {
                                System.out.println("Исходящий поток больше 75");
                                writer.write("Исходящий поток больше 75\r\n");
                                writer.write(data + "!!!Client: " + clientkey +
                                        " - Dest: " + clientdestkey +
                                        " - Client_port: " + client_ports +
                                        " - Countclient_port: " + countOut +
                                        " - Byte in: " + bytesins +
                                        " - Byte out: " + bytesouts+"\r\n");
                                System.out.println(data + "!!!Client: " + clientkey +
                                        " - Dest: " + clientdestkey +
                                        " - Client_port: " + client_ports +
                                        " - Countclient_port: " + countOut +
                                        " - Byte in: " + bytesins +
                                        " - Byte out: " + bytesouts);
                                countError = countError + 1;
                            }
                            if (bytesouts > 512) {
                                writer.write(data + "!!!Client: " + clientkey +
                                        " - Dest: " + clientdestkey +
                                        " - Client_port: " + client_ports +
                                        " - Countclient_port: " + countOut +
                                        " - Byte in: " + bytesins +
                                        " - Byte out: " + bytesouts+"\r\n");
                                System.out.println("Входящий поток больше 512");
                                System.out.println(data + "!!!Client: " + clientkey +
                                        " - Dest: " + clientdestkey +
                                        " - Client_port: " + client_ports +
                                        " - Countclient_port: " + countOut +
                                        " - Byte in: " + bytesins +
                                        " - Byte out: " + bytesouts);

                            }
                            if (countOut > 8) {
                                writer.write(data + "!!!Client: " + clientkey +
                                        " - Dest: " + clientdestkey +
                                        " - Client_port: " + client_ports +
                                        " - Countclient_port: " + countOut +
                                        " - Byte in: " + bytesins +
                                        " - Byte out: " + bytesouts+"\r\n");
                                System.out.println("В потоке больше 8");
                                System.out.println(data + "!!!Client: " + clientkey +
                                        " - Dest: " + clientdestkey +
                                        " - Client_port: " + client_ports +
                                        " - Countclient_port: " + countOut +
                                        " - Byte in: " + bytesins +
                                        " - Byte out: " + bytesouts);
                            }
                            counter++;

                        }
                        if (counter > 45) {
                            countPortMax = countPortMax + 1;
                            writer.write("В потоке больше 45 потоков" + counter+"\r\n");
                            System.out.println("В потоке больше 45 потоков" + counter);
                        }
                        counter = 0;
                        // Terms icmpresponse = entry2.getAggregations().get("icmp.response.type");
                        // if (icmpresponse != null) {
                        //for (Terms.Bucket entry3 : icmpresponse.getBuckets()) {
                        //  icmpresponses = entry3.getKeyAsString();
                        //  countOut = (int) entry3.getDocCount();


                    }
                }


            }
            System.out.println("Всего потоков: " + counALL + "Ошибочное сробатывание: " + countError + "Процент: " + countError / counALL * 100);
            System.out.println("Максимальное число портов: " + countPortMax);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}