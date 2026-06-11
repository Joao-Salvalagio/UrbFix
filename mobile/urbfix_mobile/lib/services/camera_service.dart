import 'dart:convert';
import 'package:image_picker/image_picker.dart';

class CameraService {
  final ImagePicker _picker = ImagePicker();

  Future<String?> captureImage() async {
    final xFile = await _picker.pickImage(
      source: ImageSource.camera,
      imageQuality: 50,
      maxWidth: 800,
      maxHeight: 800,
    );
    return _encodeToBase64(xFile);
  }

  Future<String?> pickFromGallery() async {
    final xFile = await _picker.pickImage(
      source: ImageSource.gallery,
      imageQuality: 50,
      maxWidth: 800,
      maxHeight: 800,
    );
    return _encodeToBase64(xFile);
  }

  Future<String?> _encodeToBase64(XFile? xFile) async {
    if (xFile == null) return null;
    final bytes = await xFile.readAsBytes();
    return 'data:image/jpeg;base64,${base64Encode(bytes)}';
  }
}
