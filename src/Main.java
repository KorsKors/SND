import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filters.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import  org.elasticsearch.transport.* ;
import org.elasticsearch.client.transport.*;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.*;
public class Main {
    public static void main(String[] args){
        TransportClient  client;
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
            .setPostFilter(QueryBuilders.rangeQuery("@timestamp").gte("now-12d").lt("now-10d"))
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
            System.out.println(entry1.getKeyAsString()+":"+entry1.getDocCount());
        }
        for (Terms.Bucket entry : dns_question_types.getBuckets()) {
            res.put(entry.getKeyAsString(),entry.getDocCount());
            System.out.println(entry.getKeyAsString()+":"+entry.getDocCount());
                }
        for (Terms.Bucket entry2 : dns_question_fullname.getBuckets()) {
            res.put(entry2.getKeyAsString(),entry2.getDocCount());
            System.out.println(entry2.getKeyAsString()+":"+entry2.getDocCount());
        }

        Terms dns_question_fullname_post=response.getAggregations().get("fullnamepost");
        Terms dns_question_name_post=response.getAggregations().get("namepost");
        Terms dns_question_types_post=response.getAggregations().get("typepost");

        for (Terms.Bucket entry1 : dns_question_name_post.getBuckets()) {
            res.put(entry1.getKeyAsString(),entry1.getDocCount());
            System.out.println(entry1.getKeyAsString()+":"+entry1.getDocCount());
        }
        for (Terms.Bucket entry : dns_question_types_post.getBuckets()) {
            res.put(entry.getKeyAsString(),entry.getDocCount());
            System.out.println(entry.getKeyAsString()+":"+entry.getDocCount());
        }
        for (Terms.Bucket entry2 : dns_question_fullname_post.getBuckets()) {
            res.put(entry2.getKeyAsString(),entry2.getDocCount());
            System.out.println(entry2.getKeyAsString()+":"+entry2.getDocCount());
        }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
    }
}