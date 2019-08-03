
bool BOOL(var value) {
  if (value == null) {
    return false;
  }
  if (value is bool) {
    return value;
  } else if (value is int) {
    return value != 0;
  } else if (value is double) {
    return value != 0.0;
  } else if (value is String) {
    return value.toLowerCase() == "true" ||
        value.toLowerCase() == "yes";
  }
  return true;
}

String STRING(var value) {
  if (value == null) {
    return "";
  }
  return value.toString();
}

int INT(var value) {
  if (value == null) {
    return 0;
  }
  if (value is bool) {
    return value ? 1 : 0;
  } else if (value is int) {
    return value;
  } else if (value is double) {
    return value.toInt();
  } else if (value is String) {
    return int.tryParse(value) ?? 0;
  }
  return 0;
}

double DOUBLE(var value) {
  if (value == null) {
    return 0.0;
  }
  if (value is bool) {
    return value ? 1.0 : 0.0;
  } else if (value is int) {
    return value.toDouble();
  } else if (value is double) {
    return value;
  } else if (value is String) {
    return double.tryParse(value) ?? 0.0;
  }
  return 0.0;
}
