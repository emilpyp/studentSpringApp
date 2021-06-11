package az.code.springappstudents.pagination;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class Pagination<T> {
    private List<T> page;
    private int totalPages;
    private int currentPage;
    private boolean hasNext = false;
    private boolean hasBefore = false;
    private String nextUrl;
    private String backUrl;

    public Pagination(List<T> items, int pageNum, int pageSize, String baseUrl){
        this.totalPages = (int) Math.ceil( (double)items.size() / (double)pageSize);
        page = items.stream().skip((long) (pageNum - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        currentPage = pageNum;
        if(currentPage < totalPages){
            hasNext = true;
            nextUrl = String.format("%spage=%d&size=%d", baseUrl, pageNum + 1, pageSize);
        }
        if(currentPage > 1){
            hasBefore = true;
            backUrl = String.format("%spage=%d&size=%d", baseUrl, pageNum - 1, pageSize);
        }
    }
}