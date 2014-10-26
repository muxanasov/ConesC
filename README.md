# ![ConesC logo](https://raw.githubusercontent.com/muxanasov/ConesC/master/ConesC.png) ConesC


ConesC brings concepts from Context-Oriented Programming (COP) down to WSN devices. Contexts model the situations that WSN software needs to adapt to. Using COP, programmers use a notion of layered function to implement context-dependent behavioral variations of WSN code. ConesC extends nesC with COP constructs. It greatly simplifies the resulting code and yields increasingly decoupled implementations compared to nesC. For example, there is a 50% reduction in the number of program states that programmers need to deal with, indicating easier debugging. In our tests, this comes at the price of a maximum 2.5% (4.5%) overhead in program (data) memory.

For more details please see [M. Afanasov et.al. "Context-Oriented Programming for Adaptive Wireless Sensor Network Software" DCOSS'14](https://www.sics.se/~luca/papers/afanasov14context.pdf)

Here you can find the dedicated translator for ConesC written in Java. Please note, that you also need [TinyOS](http://www.tinyos.net/) and a nesC toolchain for the translator to work properly.

Please, read our [Getting Started](https://github.com/muxanasov/ConesC/wiki/Getting-Started) wiki page.

If you want to know more, how to use ConesC in your project, plese, head on to [Basics of ConesC](https://github.com/muxanasov/ConesC/wiki/Basics-of-ConesC).

To get more details about ConesC, go to [Advanced ConesC](https://github.com/muxanasov/ConesC/wiki/Advanced-ConesC).

### [GrEVeCOM](https://github.com/muxanasov/GrEVeCOM)
GrEVeCOM is a collocated project which allows you to draw your context-oriented model as a graphical diagram and generate a skeleton for ConesC application. Just try it!
