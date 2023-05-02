package info.kgeorgiy.ja.isaeva.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.*;

public class WebCrawler implements AdvancedCrawler {
    Map<String, Semaphore> hosts;
    Downloader downloader;
    ExecutorService downloadService;
    ExecutorService extractService;
    int perHost;

    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        this.downloader = downloader;
        downloadService = Executors.newFixedThreadPool(downloaders);
        extractService = Executors.newFixedThreadPool(extractors);
        hosts = new ConcurrentHashMap<>();
        this.perHost = perHost;
    }

    private class Exctractor {
        Set<String> downloaded;
        Set<String> visited;
        Map<String, IOException> errors;
        List<String> hostsToVisit;

        public Exctractor(List<String> hosts) {
            this.downloaded = ConcurrentHashMap.newKeySet();
            this.visited = ConcurrentHashMap.newKeySet();
            this.errors = new ConcurrentHashMap<>();
            this.hostsToVisit = hosts;

        }

        private String getHost(String url) {
            try {
                return URLUtils.getHost(url);
            } catch (MalformedURLException e) {
                errors.put(url, e);
                return null;
            }
        }


        private void extract(Set<String> urls, int depth) {
            final Phaser phaser = new Phaser(1);
            Set<String> nextLevel = ConcurrentHashMap.newKeySet();
            for (String url : urls) {
                if (visited.contains(url)) continue;
                visited.add(url);

                String host = getHost(url);
                if (host == null || hostsToVisit != null && !hostsToVisit.contains(host)) continue;

                hosts.putIfAbsent(host, new Semaphore(perHost));

                try {
                    hosts.get(host).acquire();
                    phaser.register();
                    downloadService.submit(() -> {
                        try {
                            Document doc = downloader.download(url);
                            downloaded.add(url);
                            if (depth == 1) {
                                return;
                            }
                            phaser.register();
                            extractService.submit(() -> {
                                try {
                                    for (String cur : doc.extractLinks()) {
                                        if (!visited.contains(cur)) {
                                            nextLevel.add(cur);
                                        }
                                    }
                                } catch (IOException e) {
                                    errors.put(url, e);
                                } finally {
                                    phaser.arrive();
                                }
                            });
                        } catch (IOException e) {
                            errors.put(url, e);
                        } finally {
                            phaser.arrive();
                            hosts.get(host).release();
                        }
                    });
                } catch (InterruptedException ignored) {
                }
            }
            phaser.arriveAndAwaitAdvance();
            if (depth != 1) {
                extract(nextLevel, depth - 1);
            }
        }
    }


    @Override
    public Result download(String url, int depth) {
        Exctractor exctractor = new Exctractor(null);
        Set<String> start = ConcurrentHashMap.newKeySet();
        start.add(url);
        exctractor.extract(start, depth);
        return new Result(new ArrayList<>(exctractor.downloaded), exctractor.errors);
    }

    @Override
    public Result download(String url, int depth, List<String> hosts) {
        Exctractor exctractor = new Exctractor(hosts);
        Set<String> start = ConcurrentHashMap.newKeySet();
        start.add(url);
        exctractor.extract(start, depth);
        return new Result(new ArrayList<>(exctractor.downloaded), exctractor.errors);
    }

    @Override
    public void close() {
        extractService.shutdown();
        downloadService.shutdown();
    }
}
