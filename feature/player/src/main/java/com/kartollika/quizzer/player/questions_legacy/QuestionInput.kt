package com.kartollika.quizzer.player.questions_legacy

/*
@Composable
fun QuestionInput(modifier: Modifier, input: Input) {
  var hintsVisible by remember { mutableStateOf(input.hints.map { false }.toMutableList()) }
  LaunchedEffect(key1 = Unit) {
    Timer().schedule(timerTask {
      hintsVisible[0] = true
      hintsVisible = hintsVisible
    }, 1000)
  }

  var inputValue by remember { mutableStateOf("") }
  Column(
    modifier = modifier
  ) {
    Text(text = input.description)
    TextField(value = inputValue, onValueChange = { value: String -> inputValue = value })

    Spacer(modifier = Modifier.height(32.dp))

    ProvideTextStyle(value = MaterialTheme.typography.caption) {
      input.hints.forEachIndexed { index, hint ->
        if (hintsVisible[index]) {
          Text(text = hint)
        }
      }
    }
  }
}*/
