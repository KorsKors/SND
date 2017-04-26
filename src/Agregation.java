
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.DocValueFormat;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by George on 14.04.2017.
 */
public class Agregation {
    SearchResponse response;

    public Agregation(SearchResponse response) {
        this.response = response;
    }

    private String clientkey = null;
    private String data = null;
    private String clientdestkey = null;
    private String icmprequest = null;
    private String icmpresponses = null;
    private double bytesins = 0;
    private double bytesouts = 0;
    private Sum bytesIn;
    private Sum bytesOut;



    int countIn = 0;
    int countOut = 0;

    public void Go() {
        Histogram histogram = response.getAggregations().get("data");
        for (Histogram.Bucket entry0 : histogram.getBuckets()) {
            DateTime data = (DateTime) entry0.getKey();
            Terms clients = entry0.getAggregations().get("client_ip");
            //System.out.println(data);
            //if ( clients!= null) {
                for (Terms.Bucket entry1 : clients.getBuckets()) {
                    clientkey = entry1.getKeyAsString();
                    //System.out.println();

                    Terms clientdest = entry1.getAggregations().get("ip");

                    for (Terms.Bucket entry2 : clientdest.getBuckets()) {
                        clientdestkey = entry2.getKeyAsString();

                        Terms icpmrequest = entry2.getAggregations().get("icmp.request.type");
                        //if (icpmrequest != null) {
                            for (Terms.Bucket entry3 : icpmrequest.getBuckets()) {
                                icmprequest = entry3.getKeyAsString();
                                countIn = (int) entry3.getDocCount();

                            }

                            Terms icmpresponse = entry2.getAggregations().get("icmp.response.type");
                           // if (icmpresponse != null) {
                                for (Terms.Bucket entry3 : icmpresponse.getBuckets()) {
                                    icmpresponses = entry3.getKeyAsString();
                                    countOut = (int) entry3.getDocCount();
                                }
                                bytesIn = entry2.getAggregations().get("bytes_in");
                                bytesins = bytesIn.getValue();
                                bytesOut = entry2.getAggregations().get("bytes_out");
                                bytesouts = bytesOut.getValue();
                            /*Terms bytesin = entry2.getAggregations().get("bytes_in");
                        for (Terms.Bucket entry3 : bytesin.getBuckets()) {
                            bytesins = entry3.getKeyAsString();
                            }

                            Terms bytesout = entry2.getAggregations().get("bytes_out");
                        for (Terms.Bucket entry3 : bytesout.getBuckets()) {
                            bytesouts = entry3.getKeyAsString();
                        }*/

                               // System.out.println(data+"!!!Отправитель: " + clientkey + " - Получатель: " + clientdestkey + " - Запрос: " + icmprequest + " - Количество запросов: " + countIn + " - Ответ: " +icmpresponses  + " - Количество ответов: " + countIn + " - Байт принято: " + bytesins + " - Байт отправлено: " + bytesouts);
                        //System.out.println(data+"!!!Client: " + clientkey + " - Dest: " + clientdestkey + " - Count query: " + countIn + " - Count response: " + countIn + " - Byte in: " + bytesins + " - Byte out: " + bytesouts);
                            if(bytesins!=bytesouts||countIn>1){

                                System.out.println(data+"!!!Client: " + clientkey + " - Dest: " + clientdestkey + " - Count query: " + countIn + " - Count response: " + countIn + " - Byte in: " + bytesins + " - Byte out: " + bytesouts);

                                }
                        if(countIn>1){

                            System.out.println(data+"!!!Client: " + clientkey + " - Dest: " + clientdestkey + " - Count query: " + countIn + " - Count response: " + countIn + " - Byte in: " + bytesins + " - Byte out: " + bytesouts);

                        }
                            }
                        }
                    }
                }

            }


