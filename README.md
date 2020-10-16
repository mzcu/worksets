# Worksets

I use `Worksets` for my personal workout programming.

## Build

Run `sbt assembly` to create executable jar `target/$scalaVersion/worksets-$version`.

## Example workout generator

This 4-day workout program uses `worksetByE1RM` builder, which determines actual weight
on the bar based on past workout performance, desired RPE (rate of perceived exertion) and 
repetitions.

```scala
package worksets.program

import worksets._
import worksets.Ops._
import worksets.Predef._
import worksets.calendar._
import worksets.workouts.Dsl._

object Example4DayWorkout extends WorkoutGenerator:
  val weeklyProgram: WeeklyProgram =
    val squatDay: Workout = (
      workout
        exercise CompetitionSquat worksetByE1RM 8.rpe x 8 repeat 4
        exercise CompetitionBench worksetByE1RM 7.5.rpe x 8 repeat 3
        exercise WideGripBench worksetByE1RM 6.rpe x 9 repeat 4
      )
    val deadLiftDay: Workout = (
      workout
        exercise CompetitionDeadlift worksetByE1RM 8.rpe x 6 repeat 3
        exercise PendlayRowStandard worksetByE1RM 8.rpe x 5 repeat 3
        exercise BodyWeightPullups workset 80.kg x DynamicReps.AddOne at 7.rpe repeat 3
      )
    List[(Day, Workout)](
      Monday -> squatDay,
      Tuesday -> deadLiftDay,
      Thursday -> squatDay,
      Friday -> deadLiftDay
    )
```

## My workflow

### Start a training block

1. Decide on next block strategy and implement new program in `worksets.program` if needed
2. Set the desired workout generator in `worksets.Config`

### Run existing training block

#### Generate weekly workout

1. Run `./worksets programNextWeek` review and save the weekly program
2. Print the program and bring it to the weight room

#### Train

Follow the program and mark the completed sets on the printed workout plan

#### Log results

Run `./worksets enterResults` for each workout day, adjusting the values which deviate
from planned ones

#### Analyze

Check the trends (`./worksets report`) and modify the workout generator if needed:

```text
$ ./target/scala-0.27/worksets-0.1 report

                 Deadlift /w Belt                Squat /w Belt                   Bench Press
                     3180 ┤╭───────╮                 3800 ┤     ╭───                 2560 ┤ ╭───────
                     2385 ┤│       ╰                 2850 ┤╭────╯                    1920 ┤╭╯
Volume               1590 ┤│                         1900 ┤│                         1280 ┤│
                      795 ┤│                          950 ┤│                          640 ┤│
                        0 ┼╯                            0 ┼╯                            0 ┼╯

                 Deadlift /w Belt                Squat /w Belt                   Bench Press
                        8 ┤╭────────                    8 ┤╭────────                    8 ┤╭────────
                        6 ┤│                            6 ┤│                            6 ┤│
Intensity               4 ┤│                            4 ┤│                            4 ┤│
                        2 ┤│                            2 ┤│                            2 ┤│
                        0 ┼╯                            0 ┼╯                            0 ┼╯

                 Deadlift /w Belt                Squat /w Belt                   Bench Press
                      180 ┤  ╭╮                       129 ┤  ╭─────                   111 ┤╭───────
                      172 ┼╮╭╯╰───╮                   128 ┤  │                        109 ┤│
E1RM                  164 ┤╰╯     │                   127 ┤  │                        106 ┤│
                      157 ┤       │                   127 ┤  │                        104 ┤│
                      149 ┤       ╰                   126 ┼──╯                        102 ┼╯

                  2020-W31 .. 2020-W42
                    34420 ┤         ╭─
                    27724 ┤    ╭─╮╭─╯
Weekly vol.         21028 ┼╮   │ ││
                    14331 ┤╰─╮ │ ╰╯
                     7635 ┤  ╰─╯

```

#### Repeat

After finishing workout week and weekly review, [generate a new weekly workout program](#generate-weekly-workout)

## Other

### RPE

- RPE calculations are based on [Mike Tuchscherer's RPE tables](https://articles.reactivetrainingsystems.com/2015/11/29/beginning-rts/)
