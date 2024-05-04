package com.busik.busik.Driver;

public interface DPassangerRequest {
    void accept(int id);
    void cancel(int id);
    void call(String phone);
    void review(int id);
}
