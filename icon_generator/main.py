# Fuck tkinter
import PyQt5.QtWidgets
import functools

__SIZE__ = 13

app = PyQt5.QtWidgets.QApplication([])
app.setStyle("Fusion")
window = PyQt5.QtWidgets.QWidget()
layout = PyQt5.QtWidgets.QGridLayout()
window.setLayout(layout)
buttons = list()


def clicked(index: int):
    btn, value = buttons[index]
    value = not value
    btn.setStyleSheet("background-color: " + "red" if value else "white")
    buttons[index] = (btn, value)


for x in range(__SIZE__):
    for y in range(__SIZE__):
        btn = PyQt5.QtWidgets.QPushButton()
        btn.clicked.connect(functools.partial(clicked, (x * __SIZE__) + y))
        btn.setMinimumSize(24, 24)
        btn.setMaximumSize(24, 24)
        buttons.append((btn, False))
        layout.addWidget(btn, x, y)

window.setMinimumSize(24 * __SIZE__, 24 * __SIZE__)
window.setMaximumSize(24 * __SIZE__, 24 * __SIZE__)
window.setWindowTitle("Close when done")
window.show()
app.exec()

var_name, _ = PyQt5.QtWidgets.QInputDialog.getText(None, "Input", "Enter the desired name for your icon: (SCREAMING_SNAKE_CASE)")

if not var_name or len(var_name.strip()) == 0:
    var_name = "PLACEHOLDER_NAME"

print(f"    public static final boolean[][] {var_name} = new boolean[][] \u007b")

width = 0

for index, (_, value) in enumerate(buttons):
    column = index % __SIZE__
    if value and column > width:
        width = column

for index, (_, value) in enumerate(buttons):
    column = index % __SIZE__
    val = str(value).lower()

    if width == 0:
        print(f"            new boolean[] {{ {val} }},")
    elif column == 0:
        print(f"            new boolean[] {{ {val}, ", end="")
    elif column == width:
        print(f"{val} }},")
    elif column < width:
        print(f"{val}, ", end="")

print("    };")
