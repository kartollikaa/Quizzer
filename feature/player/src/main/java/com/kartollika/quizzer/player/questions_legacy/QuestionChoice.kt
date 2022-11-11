package com.kartollika.quizzer.player.questions_legacy

/*
@Composable
fun QuestionChoice(
  modifier: Modifier = Modifier,
  choice: Choice,
  onAnswerChange: (Answer) -> Unit
) {
  var answer by remember { mutableStateOf(Answer.Choice()) }

  Column(modifier = modifier) {
    Text(modifier = Modifier.padding(16.dp), text = choice.description)
    Spacer(modifier = Modifier.height(16.dp))

    choice.options.forEach { option ->
      ChoiceOption(
        modifier = Modifier.fillMaxWidth(),
        option = option,
        multipleSelection = choice.correctOptions.size > 1,
        selected = answer.options.contains(option),
        onSelectionChanged = { selected ->
          val list = answer.options.toMutableList()
          if (!selected) {
            list.remove(option)
          } else {
            list.add(option)
          }
          answer = answer.copy(options = list)
          onAnswerChange(answer)
        }
      )
    }
  }
}

@Composable
private fun ChoiceOption(
  modifier: Modifier = Modifier,
  option: Option,
  multipleSelection: Boolean = false,
  selected: Boolean,
  onSelectionChanged: (Boolean) -> Unit = {}
) {
  Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
    if (multipleSelection) {
      Checkbox(checked = selected, onCheckedChange = {
        onSelectionChanged(selected)
      })
    } else {
      RadioButton(selected = selected, onClick = {
        onSelectionChanged(selected)
      })
    }
    Text(text = option.text)
  }
}

@Preview(showBackground = true)
@Composable
fun ChoicePreview() {
  ChoiceOption(option = Option("123"), multipleSelection = false, selected = false)
}*/
