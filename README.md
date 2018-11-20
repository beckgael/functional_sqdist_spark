# An humble example showing performance improving from functional approach over imperativ one

I know it's not the more rigorous way to make speedtest and some specifics tools exist in order to achieve it (never try them, i should !) but results difference is important enough to let thing that there are some underlying reasons to prefer functional approach compare to imperativ one. I don't know which ones, probably some funny compilator optimizations but i'm sure it is way to explore. I will be happy to do more testing with better tools if i have the occasion.

## A SparkNotebook showing my experiments is available in this repo if you want to try by yourself.

## Process

I took two version of `sqdist`, imparativ and functional ones. For input vector size i run 60% of warming up `sqdist` and measure time on the left 40% for different number of iterations from 100k to 10M. At each iterations 2 random DenseVector are generated. Code spoke better by himself i think.

## Beginning of possible explanations

It can be found on theses StackOverFlow, [this one](https://stackoverflow.com/questions/9168624/why-is-my-scala-tail-recursion-faster-than-the-while-loop) which highlight a question of JVM version, or [this other one](https://stackoverflow.com/questions/32324859/is-there-any-way-to-turn-off-tail-recursion-optimization-of-scala-compiler) which propose to prevent tailrecursion optimization which can be usefull to see at which degree theses optimizations are important.

### Have a happy sparking day !