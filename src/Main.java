import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.EntryMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.util.Supplier;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.client.transport.*;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args){
        TransportClient  client;
        // String ms []=new String[2];
        ArrayList<String> ms=new ArrayList<String>();
        ms.add("google.com.");
        ms.add("youtube.com.");
        Map<String, Object> res=new HashMap<String, Object>();
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

            SearchResponse response = client.prepareSearch("packetbeat-*").setTypes("dns")
                    .setQuery(QueryBuilders.boolQuery()
                            .must(QueryBuilders.rangeQuery("@timestamp").gte("now-26d").lt("now-23d")))

                    .addAggregation(AggregationBuilders
                            .dateHistogram("data")
                            .field("@timestamp")
                            .dateHistogramInterval(DateHistogramInterval.seconds(30))
                            //.terms("client_ip")
                            //.field("client_ip")
                            //.size(10000)
                            .subAggregation(AggregationBuilders.terms("client_ip").field("client_ip").size(10000)
                                    .subAggregation(AggregationBuilders.terms("ip").field("ip").size(10000)
                                            .subAggregation(AggregationBuilders.terms("client_port").field("client_port").size(10000)
                                            .subAggregation(AggregationBuilders.avg("bytes_in").field("bytes_in"))
                                            .subAggregation(AggregationBuilders.avg("bytes_out").field("bytes_out"))))))
                    .execute().actionGet();

            AggDNS agg = new AggDNS(response);
            agg.go();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }}


    public static void mains(String[] args){
        TransportClient  client;
        // String ms []=new String[2];
        ArrayList<String> ms=new ArrayList<String>();
        ms.add("google.com.");
        ms.add("youtube.com.");
        Map<String, Object> res=new HashMap<String, Object>();
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

            SearchResponse response = client.prepareSearch("packetbeat-*").setTypes("icmp")
                    .setQuery(QueryBuilders.boolQuery()
                            .must(QueryBuilders.rangeQuery("@timestamp").gte("now-26d").lt("now-23"))
                            .must(QueryBuilders.termsQuery("icmp.request.type","8"))
                            .must(QueryBuilders.termsQuery("icmp.response.type","0"))
                            .mustNot(QueryBuilders.termsQuery("dns.question.etld_plus_one", ms)))

                    .addAggregation(AggregationBuilders
                            .dateHistogram("data")
                            .field("@timestamp")
                            .dateHistogramInterval(DateHistogramInterval.seconds(30))
                            //.terms("client_ip")
                            //.field("client_ip")
                            //.size(10000)
                                    .subAggregation(AggregationBuilders.terms("client_ip").field("client_ip").size(10000)
                                    .subAggregation(AggregationBuilders.terms("ip").field("ip").size(10000)
                                    .subAggregation(AggregationBuilders.terms("icmp.request.type").field("icmp.request.type").size(10000))
                                    .subAggregation(AggregationBuilders.terms("icmp.response.type").field("icmp.response.type").size(10000))
                                    .subAggregation(AggregationBuilders.sum("bytes_in").field("bytes_in"))
                                    .subAggregation(AggregationBuilders.sum("bytes_out").field("bytes_out")))))
                    .execute().actionGet();
            Agregation agg = new Agregation(response);
            agg.Go();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }}
    public static void main2(String[] args){
        TransportClient  client;
       // String ms []=new String[2];
        ArrayList<String> ms=new ArrayList<String>();
        ms.add("google.com.");
        ms.add("youtube.com.");
        Map<String, Object> res=new HashMap<String, Object>();
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

            SearchResponse response = client.prepareSearch("packetbeat-*").setTypes("dns")
                    .setQuery(QueryBuilders.boolQuery()
                            .must(QueryBuilders.rangeQuery("@timestamp").gte("now-12d").lt("now-10d"))
                            .mustNot(QueryBuilders.termsQuery("dns.question.etld_plus_one", ms)))

                    .addAggregation(AggregationBuilders
                            .terms("name").field("dns.question.etld_plus_one").size(5))
                    .addAggregation(AggregationBuilders
                            .terms("type").field("dns.question.type").size(5))
                    .execute().actionGet();
            Terms dns_question_name=response.getAggregations().get("name");
            Terms dns_question_type = response.getAggregations().get("type");
                for (Terms.Bucket entry1 : dns_question_name.getBuckets()) {
                res.put(entry1.getKeyAsString(), entry1.getDocCount());
                System.out.println("Domain:" + entry1.getKeyAsString() + ":" + entry1.getDocCount());
                }
                for (Terms.Bucket entry2 : dns_question_type.getBuckets()) {
                    res.put(entry2.getKeyAsString(), entry2.getDocCount());
                    System.out.println("Type:" + entry2.getKeyAsString() + ":" + entry2.getDocCount());
                }
            } catch (UnknownHostException e) {
            e.printStackTrace();
        }}
    public static void mainsq(String[] args){
        TransportClient  client;
        String ms []=new String[10];
        ms[0]="google.com.";
        ms[1]="alitools.io.";
        Map<String, Object> res=new HashMap<String, Object>();
            try {
                client = new PreBuiltTransportClient(Settings.EMPTY)
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

            SearchResponse response = client.prepareSearch("packetbeat-2017.03.12")
            .setQuery(QueryBuilders.rangeQuery("@timestamp").gte("now-12d").lt("now-10d"))
                .addAggregation(AggregationBuilders
                        .terms("name").field("dns.question.name").size(5))
                .addAggregation(AggregationBuilders
                        .terms("type").field("dns.question.type").size(5))
                    .addAggregation(AggregationBuilders
                            .terms("fullname").field("dns.question.etld_plus_one").size(5))
            .setPostFilter(QueryBuilders.rangeQuery("@timestamp").gte("now-1d").lt("now-1d"))
                .addAggregation(AggregationBuilders
                    .terms("namepost").field("dns.question.name").size(2))
                .addAggregation(AggregationBuilders
                        .terms("typepost").field("dns.question.type").size(2))
                .addAggregation(AggregationBuilders
                        .terms("fullnamepost").field("dns.question.etld_plus_one").size(2))
            .execute().actionGet();

        Terms dns_question_fullname=response.getAggregations().get("fullname");
        Terms dns_question_name=response.getAggregations().get("name");
        Terms dns_question_types=response.getAggregations().get("type");

        for (Terms.Bucket entry1 : dns_question_name.getBuckets()) {
            res.put(entry1.getKeyAsString(),entry1.getDocCount());
            //System.out.println("Domain:"+entry1.getKeyAsString()+":"+entry1.getDocCount());
        }
        for (Terms.Bucket entry : dns_question_types.getBuckets()) {
            res.put(entry.getKeyAsString(),entry.getDocCount());
            //System.out.println("Type:"+entry.getKeyAsString()+":"+entry.getDocCount());
                }
        for (Terms.Bucket entry2 : dns_question_fullname.getBuckets()) {
            res.put(entry2.getKeyAsString(),entry2.getDocCount());
            System.out.println("Domain:"+entry2.getKeyAsString()+":"+entry2.getDocCount());
        }

        Terms dns_question_fullname_post=response.getAggregations().get("fullnamepost");
        Terms dns_question_name_post=response.getAggregations().get("namepost");
        Terms dns_question_types_post=response.getAggregations().get("typepost");
                System.out.println();
        for (Terms.Bucket entry1 : dns_question_name_post.getBuckets()) {
            res.put(entry1.getKeyAsString(),entry1.getDocCount());
            //System.out.println(entry1.getKeyAsString()+":"+entry1.getDocCount());
        }
        for (Terms.Bucket entry : dns_question_types_post.getBuckets()) {
            res.put(entry.getKeyAsString(),entry.getDocCount());
            //System.out.println(entry.getKeyAsString()+":"+entry.getDocCount());
        }
        for (Terms.Bucket entry2 : dns_question_fullname_post.getBuckets()) {
            res.put(entry2.getKeyAsString(),entry2.getDocCount());
            System.out.println("DomainNow"+entry2.getKeyAsString()+":"+entry2.getDocCount());
        }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
    }
}