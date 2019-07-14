import matplotlib.pyplot as plt
import numpy as np
import pylab
t, x, y = np.loadtxt('data.txt', delimiter=' ',unpack=True)

pylab.plot(t, x, '-b', label='tempMin')
pylab.plot(t, y, '-r', label='tempMax')
plt.xlabel('time[s]')
plt.ylabel('tempMin/tempMax[s]')
plt.title('Wykres temperatur dla TEST CASE 2')
plt.legend()
plt.show()
