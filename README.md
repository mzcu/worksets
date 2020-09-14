# Worksets

I use `Worksets` for my personal workout programming.

## Example workout generator

This 4-day workout program uses `worksetByE1RM` builder, which determines actual weight
on the bar based on past workout performance, desired RPE (rate of perceived exertion) and 
repetitions.

```scala
package worksets.program

import worksets.Predef._
import worksets._
import worksets.calendar._
import worksets.workouts.Dsl._

object Hypertrophy4Day extends WorkoutGenerator {
  val weeklyProgram: WeeklyProgram = {
    val squatDay: Workout = (
      workout
        exercise CompetitionSquat worksetByE1RM 8.rpe x 8 repeat 3
        exercise CompetitionBench worksetByE1RM 7.5.rpe x 8 repeat 3
        exercise WideGripBench worksetByE1RM 6.rpe x 9 repeat 3
      )
    val deadLiftDay: Workout = (
      workout
        exercise CompetitionDeadlift worksetByE1RM 8.rpe x 6 repeat 3
        exercise PendlayRowStandard worksetByE1RM 8.rpe x 5 repeat 3
        exercise BodyWeightPullups workset 80.kg x 5 at 7.rpe repeat 3
      )
    List[(Day, Workout)](
      Monday -> squatDay,
      Tuesday -> deadLiftDay,
      Thursday -> squatDay,
      Friday -> deadLiftDay
    )
  }
}
```

## My workflow

### Start a training block

1. Decide on next block strategy and implement new program in `worksets.program` if needed
2. Set the desired workout generator in `worksets.Config`

### Run existing training block

#### Generate weekly workout

1. Run `programNextWeek` command after starting `./repl.sh`, review and save the weekly program
2. Print the program and bring it to the weight room

#### Train

Follow the program and mark the completed sets on the printed workout plan

#### Log results

1. Start `./repl.sh` and run `enterResults` for each workout day, adjusting the values which deviate
from planned ones
2. Check progress on E1RM (`report.e1rm`) or volume (`report.volumeProgression`) report

#### Repeat

After finishing workout week and weekly review, [generate a new weekly workout program](#generate-weekly-workout)

## Other

### RPE

- RPE calculations are based on [Mike Tuchscherer's RPE tables](https://articles.reactivetrainingsystems.com/2015/11/29/beginning-rts/)
