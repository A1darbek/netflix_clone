package com.ayderbek.netflxSpring.netflix_clone.service;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import lombok.RequiredArgsConstructor;

import java.io.Closeable;
import java.io.IOException;

@RequiredArgsConstructor
public class TransferManagerCloseable implements Closeable, AutoCloseable{
    private final TransferManager transferManager;
    private final S3Object s3Object;

    @Override
    public void close() throws IOException {
        transferManager.shutdownNow();
        s3Object.close();
    }

}
