# Kibana Reports for Open Distro

Kibana Reports for Open Distro allows ‘Report Owner’ (engineers, including but not limited to developers, DevOps, IT Engineer, and IT admin) export and share reports from Kibana dashboards, saved search, alerts and visualizations. It helps automate the process of scheduling reports on an on-demand or a periodical basis (on cron schedules as well). Further, it also automates the process of exporting and sharing reports triggered for various alerts. The feature is present in the Dashboard, Discover, and Visualization tabs. Scheduled reports can be sent to (shared with) self or various stakeholders within the organization such as, including but not limited to, executives, managers, engineers (developers, DevOps, IT Engineer) in the form of pdf, hyperlinks, csv, excel via various channels such as email, slack, Amazon Chime. However, in order to export, schedule and share reports, report owners should have the necessary permissions as defined under Roles and Privileges.

# Request for Comments ( RFC )

Please add your feature requests here [ New Requests ](https://github.com/opendistro-for-elasticsearch/kibana-reports/issues) and view project progress here [RFCs](https://github.com/opendistro-for-elasticsearch/kibana-reports/projects/1).

## Setup & Build

Complete Kibana Reports is composed of 3 plugins. Refer to README in each plugin folder for more details.

- [Kibana reports plugin](./kibana-reports/README.md)
- [Reports scheduler ES plugin](./reports-scheduler/README.md)（TODO）
- [Reports notification ES plugin]() (TODO)

## Contributing to Kibana reports for Open Distro

We welcome you to get involved in development, documentation, testing the kibana reports plugin. See our [CONTRIBUTING.md](./CONTRIBUTING.md) and join in.

Since this is a Kibana plugin, it can be useful to review the [Kibana contributing guide](https://github.com/elastic/kibana/blob/master/CONTRIBUTING.md) alongside the documentation around [Kibana plugins](https://www.elastic.co/guide/en/kibana/master/kibana-plugins.html) and [plugin development](https://www.elastic.co/guide/en/kibana/master/plugin-development.html).

## Code of Conduct

This project has adopted an [Open Source Code of Conduct](https://opendistro.github.io/for-elasticsearch/codeofconduct.html).

## Security issue notifications

If you discover a potential security issue in this project we ask that you notify AWS/Amazon Security via our [vulnerability reporting page](http://aws.amazon.com/security/vulnerability-reporting/). Please do **not** create a public GitHub issue.

## License

See the [LICENSE](./LICENSE.txt) file for our project's licensing. We will ask you to confirm the licensing of your contribution.

## Copyright

Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
