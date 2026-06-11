import 'package:flutter/material.dart';
import 'core/app_theme.dart';
import 'views/home_screen.dart';

void main() {
  runApp(const UrbFixApp());
}

class UrbFixApp extends StatelessWidget {
  const UrbFixApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'UrbFix',
      theme: AppTheme.theme,
      debugShowCheckedModeBanner: false,
      home: const HomeScreen(),
    );
  }
}
