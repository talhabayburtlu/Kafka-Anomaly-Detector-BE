# Kafka Anomaly Detector [Backend]

A tool for analyzing kafka clusters and creating alerts when anomalies are detected. 🤖 :chart_with_upwards_trend:

## Description
Kafka Anomaly Detector is a tool for analyzing unconsummed messages (lags) between kafka topics and consumer groups. It's main idea is to notify people when lags are out of defined boundaries. It is composed of three different modules which are responsible from interactions with api, lag analyzer scheduler and alert processor scheduler.

## Definitions
- Rules are ways of maintaining boundaries between topic and consumer group pairs. :straight_ruler:
- Regex relations are ways of remainding users to create rules between matching topic and consumer group pairs. :pushpin:

## Modules
- :computer: Interactions: Exposes API to the user for managing kafka clusters, creating rules for topic & consumer group pairs, defining target and destinations for alerts to be sent.
- :chart_with_upwards_trend: Lag Analyzer: Is a scheduler which iterates over all clusters and their topic & consumer group pairs for finding lags between them. Lags are saved to the PostgreSQL database. If there is a rule defined for that pair and the boundaries are crossed error alerts are produced. If any regex relations found between not processed pairs creates warning alerts.
- :exclamation: Alert Processor: Is a scheduler which consumes unprocessed alerts by looking their target and destinations. Currently slack channel notification system is available.


## Features
- Multiple kafka cluster analysis available by giving their bootstrap address with optionally their security configurations to the application.
- For topic and consumer group pairs rules can be defined with upper and lower boundary. Alerts will be created when those boundaries are crossed.
- Regex relations remainds important topic and consumer groups which needed to be analzyed via defining rules.
- A target reflects where alert should be sent when processed. Currently only slack channel notification system is available via webhooks feature.
- A destination is composed with multiple targets for each community to get their notifications. Each rules and regex needed to be defined with a destination.

## Deployment
Each module has their Dockerfile which exports jar files and creates docker images. Dockerfiles can be build under module directory.  
```sh
$ docker build -t <module-name> .
$ docker run -p 8081:8081 <module-name>
```
Interactions API can be runned with docker but lag analyzer and alert processor are needed to be run with Kubernetes cron jobs which each of them has their cron.yaml file.  
```sh
$ kubectl create -f cron.yaml
```

## Screenshots
![Slack Notifications](https://i.imgur.com/SizYi7H.png)  

## Planned to Add
[x] Slack notification Integration. :bell:  
[ ] Email notification integration. :bell:  
[ ] SMS notification integration. :bell:  
[ ] Different boundaries to be set for time intervals for rules in order to avoid high traffics to be mark as anomalies. :straight_ruler:  
