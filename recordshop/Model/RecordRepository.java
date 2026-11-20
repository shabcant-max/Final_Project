package recordshop.model;

import java.util.List;


public interface RecordRepository {
    List<Record> findAll();
    Record findById(String id);
    Record save(Record record);
    void deleteById(String id);
}