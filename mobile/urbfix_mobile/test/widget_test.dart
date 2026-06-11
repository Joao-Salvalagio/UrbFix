import 'package:flutter_test/flutter_test.dart';
import 'package:urbfix_mobile/main.dart';

void main() {
  testWidgets('App smoke test', (WidgetTester tester) async {
    await tester.pumpWidget(const UrbFixApp());
    expect(find.byType(UrbFixApp), findsOneWidget);
  });
}
