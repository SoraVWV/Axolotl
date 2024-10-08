## Shunting Yard для построения AST с контекстом состояний

Алгоритм, описываемый как `модифицированный Shunting Yard с состояниями
для построения AST`, представляет собой расширение классического Shunting Yard для работы с синтаксическими
конструкциями, сочетая элементы восходящего синтаксического анализа с ручным управлением состояниями. Алгоритм
обрабатывает синтаксические структуры для построения абстрактного синтаксического дерева (AST) с помощью трех стеков:
для операторов, состояний и промежуточных результатов.

Формулировка алгоритма:

1. Инициализация:

    1. Создаются три стека:

        - Стек операторов — хранит операторы выражений.

        - Стек состояний — отслеживает контекст синтаксического анализа.

        - Результативный стек — содержит операнды и узлы AST.

    2. Начальное состояние синтаксического анализатора соответствует начальному начальному состоянию файла языка или
       состоянию фрейма лексического анализатора.

2. Чтение токенов:

   Лексический анализатор подает на вход синтаксическому анализатору.

3. Обработка токенов:

    1. Операнды (например, числа, вызовы функций) добавляются в результативный стек.

    2. Операторы:

        - Проверяется приоритет с оператором на вершине стека операторов.

        - Если приоритет текущего оператора ниже или равен, выполняется редукция оператор извлекается, и на основе
          операндов из результативного стека строится узел AST.

        - Новый оператор добавляется в стек операторов.

    3. Открывающая управляющая конструкция конструкция изменяет состояние синтаксического анализа через стек состояний.

    4. Закрывающая управляющая конструкция запускает редукцию, завершая текущую операцию и создавая узел AST.

4. Переходы состояний

   При обнаружении конструкций стек состояний обновляется, чтобы отслеживать текущий контекст анализа (например,
   выражение или аргументы метода).

5. Редукция:

   Когда завершается синтаксическая конструкция (например, закрывающая скобка), происходит редукция:

    1. Оператор извлекается из стека операторов.

    2. Операнды извлекаются из результативного стека.

    3. Создается узел AST, который возвращается в результативный стек.

6. Завершение:

   После обработки всего фрейма лексического анализатора, синтаксический анализатор берет оставшиеся элементы в
   результативном стеке и стеке операторов, создавая итоговый узел AST.

7. Восстановление после ошибок

   При обнаружении синтаксической ошибки алгоритм фиксирует текущее состояние и пытается восстановить корректность путем
   удаления неправильных токенов и игнорирования конструкций.

Время

- Основные операции, такие как добавление токенов, проверка операторов и редукция, выполняются за O(1) на каждый шаг, но
  в худшем случае редукция может занять O(n) при сложных выражениях.

- Общая сложность алгоритма по времени — O(n), так как каждая операция привязана к количеству токенов.

Память

- Все три стека (операторы, состояния, результат) могут содержать до n элементов в худшем случае, что дает
  пространственную сложность O(n).

- Конечное AST также занимает O(n) памяти, пропорционально количеству токенов в исходном коде.

