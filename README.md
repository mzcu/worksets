# Worksets

I use `Worksets` for my personal workout programming.

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
