import re
import sys

app_name = {{ cookiecutter.app_name | tojson }}
application_id = "{{ cookiecutter.application_id }}"
namespace = "{{ cookiecutter.namespace }}"
min_sdk = {{ cookiecutter.min_sdk }}

if not app_name.strip() or re.search(r'["\\/]', app_name):
    print(f"ERROR: '{app_name}' is not a valid app name.")
    print('Must not be empty or contain ", \\ or /')
    sys.exit(1)

if not re.match(r"^[a-z][a-z0-9]*(\.[a-z][a-z0-9]*){2,}$", application_id):
    print(f"ERROR: '{application_id}' is not a valid application ID.")
    print("Must be lowercase, at least 3 segments (e.g. com.example.myapp)")
    sys.exit(1)

if not re.match(r"^[a-z][a-z0-9]*(\.[a-z][a-z0-9]*){1,}$", namespace):
    print(f"ERROR: '{namespace}' is not a valid Kotlin namespace.")
    print("Must be lowercase, at least 2 segments (e.g. com.example)")
    sys.exit(1)

KOTLIN_KEYWORDS = {
    "as", "break", "class", "continue", "do", "else", "false", "for", "fun",
    "if", "in", "interface", "is", "null", "object", "package", "return",
    "super", "this", "throw", "true", "try", "typealias", "typeof", "val",
    "var", "when", "while",
}
for segment in namespace.split("."):
    if segment in KOTLIN_KEYWORDS:
        print(f"ERROR: Namespace segment '{segment}' is a Kotlin keyword.")
        sys.exit(1)

if not (23 <= min_sdk <= 37):
    print(f"ERROR: minSdk {min_sdk} is out of range (23-37).")
    sys.exit(1)
