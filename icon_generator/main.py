# Fuck tkinter
import PyQt5.QtWidgets
import functools

__SIZE__ = 12

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
window.setWindowTitle("Something something make my life less tedious")
window.show()
app.exec()

var_name, _ = PyQt5.QtWidgets.QInputDialog.getText(None, "Input", "Enter the desired name for your icon: (SCREAMING_SNAKE_CASE)")

if not var_name or var_name.strip().__len__() == 0:
    var_name = "PLACEHOLDER_NAME"

print("    public static final boolean[][] " + var_name + " = new boolean[][] {")

width = 0

for index in range(buttons.__len__()):
    _, value = buttons[index]
    column = index % __SIZE__
    if value and index > width:
        width = column

for index in range(buttons.__len__()):
    _, value = buttons[index]
    column = index % __SIZE__
    if width == 0 and column == 0:
        print("            new boolean[] { " + str(value).lower() + " },")
    elif column == 0:
        print("            new boolean[] { " + str(value).lower() + ", ", end="")
    elif column == width + 1:
        print(str(value).lower() + " },")
    elif column > 0 and column < width:
        print(str(value).lower() + ", ", end="")
print("    };")
