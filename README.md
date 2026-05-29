# haptic4j

This is a small tool for testing force feedback devices
similar to fftest(1). It's explicitly for testing of the
rumble effect at different strength. Reference: [www.kernel.org](https://www.kernel.org/doc/html/latest/input/ff.html)

The project specifically focuses on integration of Java and C++
with JNI and FFM (Project Panama).

### **Usage:**

Fill up the `.bashrc` file with paths on your machine, then run
```bash
source .bashrc
```

In destined folder do:
```bash
./build.sh && ./run.sh
```
