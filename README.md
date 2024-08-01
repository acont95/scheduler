<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a name="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]



<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/acont95/scheduler">
    <img src="images/flat-g997b39d5c_640.png" alt="Logo" width="80">
  </a>

<h3 align="center">Java Scheduler</h3>

  <p align="center">
    A simple scheduler for Java with support for testing over arbitrary periods of time. 
    <br />
    <a href="https://github.com/acont95/flight-controller"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/acont95/flight-controller">View Demo</a>
    ·
    <a href="https://github.com/acont95/flight-controller/issues">Report Bug</a>
    ·
    <a href="https://github.com/acont95/flight-controller/issues">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

<!-- ![Product Name Screen Shot][product-screenshot] -->

The goal of this project is to develop a simple to use multi threaded task scheduler that can be integreated into any Java application. A simulation runtime is included which can test your application by incrementing time over arbitrary periods and time steps. 

<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Built With

* [![Java][Java]][Java-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started


### Prerequisites


* Java 17+

## Installation

### Maven

Functionality of this package is contained in the Java package org.ac.scheduler.

To use this package, add the following Maven dependency to your project (WIP):

```xml
<dependency>
    <groupId>org.ac.scheduler</groupId>
    <artifactId>scheduler</artifactId>
    <version>${scheduler.version}</version>
</dependency>
```

### Non-Maven

For non-maven use cases, you can download a JAR from the Maven Central Repository. (WIP)
<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage
* Create a runtime object. A SimulationRuntime can be created which use's an "artificial" clock (specifically MutableClock from ThreeTen Extra).  Set a user defined start time, end time, step, and step delay:
```java
SimulationConfig simulationConfig = new SimulationConfig(
    ZonedDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")), //start
    ZonedDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")), //end
    Duration.ofDays(1), //step
    Duration.ZERO //pause between step
);
SchedulerRuntime runtime = new SimulationRuntime(simulationConfig);
```

Alternatively, create a WallClockRuntime object to use the SystemUTC clock: 

```java
SchedulerRuntime runtime = new WallClockRuntime();
```

* Create the scheduler object:
```java
Scheduler s = new Scheduler(
  2 //number of executor threads
);
```

* Define a schedule to run tasks:
```java
ScheduleDefine sched = PeriodSchedule.Builder.getInstance()
  .every(Period.ofDays(1))
  .build();
```

* Extend Callable<Void> to define your callable class:
```java
class TestSchedulerCallable extends Callable<Void> {

    @Override
    public Void call() {
        System.out.println(Instant.now(clock));
        return null;
    }
}
```

* Create a schedule task:
```java
ScheduledTask task = new SingleScheduledTask("print_instant_daily_task", new TestSchedulerCallable(), sched);

```

* Pass your task to the scheduler and run:
```java
s.schedule(
  task,
  runtime.getClock().instant() //scheduled time
);
runtime.start(s);
```

_For more examples, please refer to the [Documentation](https://example.com)_

### Currently implemented schedules:
* Day of month
* Day of week
* Interval (Duration)
* Local time
* Month day
* Month
* Weekday
* Period

Schedules can be combined into a MultiScheduledTask object which will only run when each schedules shouldRun method evaluates to true: 

```java
List<ScheduleDefine> scheduleList = new ArrayList<>();
scheduleList.add(sched)
...
ScheduledTask task = new MultiScheduledTask("print_instant_daily_task", new TestSchedulerCallable(), scheduleList);

```

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- ROADMAP -->
## Roadmap

- [ ] Add optional task persistence.
- [X] Better support for logical combination of schedules to create more complex schedules.
- [X] Use JSON file for simulation config.


See the [open issues](https://github.com/acont95/flight-controller/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->
## Contact

Alex Conticello - conticello.alex@gmail.com

Project Link: [https://github.com/acont95/flight-controller](https://github.com/acont95/flight-controller)



<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/acont95/flight-controller.svg?style=for-the-badge
[contributors-url]: https://github.com/acont95/flight-controller/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/acont95/flight-controller.svg?style=for-the-badge
[forks-url]: https://github.com/acont95/flight-controller/network/members
[stars-shield]: https://img.shields.io/github/stars/acont95/flight-controller.svg?style=for-the-badge
[stars-url]: https://github.com/acont95/flight-controller/stargazers
[issues-shield]: https://img.shields.io/github/issues/acont95/flight-controller.svg?style=for-the-badge
[issues-url]: https://github.com/acont95/flight-controller/issues
[license-shield]: https://img.shields.io/github/license/acont95/flight-controller.svg?style=for-the-badge
[license-url]: https://github.com/acont95/flight-controller/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/alex-conticello-8555bb101
[product-screenshot]: images/air-drone-icon.png

[Java]: https://img.shields.io/badge/-java-blue?style=for-the-badge&logo=java&logoColor=white
[Java-url]: https://www.java.com/en/