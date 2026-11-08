# Assignment 4 Report: Dynamic Programming and String Algorithms

---

## Experiment Design Card

| Card Field | Description |
| :--- | :--- |
| **Question** | 1. **Edit Distance:** How much performance overhead does recursion introduce compared to DP, and what is the space-time trade-off between Table and Two-Row approaches?<br>2. **String Search:** How does the KMP algorithm perform relative to Naive search when facing worst-case highly repetitive pattern inputs? |
| **Sizes ($n$)** | - **Edit Distance Recursive:** $n \in \{4, 8, 12, 16\}$ (due to $O(3^n)$ exponential complexity context).<br>- **Edit Distance DP:** $n \in \{100, 500, 1000, 2000\}$ (sufficient scale without timing noise).<br>- **String Search:** $n \in \{1000, 5000, 20000, 100000\}$ (demonstrates linear scaling vs quadratic scaling). |
| **Inputs** | - **Random Strings:** Character strings from `[A-Za-z0-9]`.<br>- **Worst Case Edit Distance:** Completely distinct characters (e.g. `AAAA...` vs `BBBB...`).<br>- **Worst Case String Search:** Pattern `AAAA...B` in Text `AAAA...A` (forces maximum character backtracking in Naive). |
| **Metrics** | - **Execution Time (ms):** Wall-clock time averaged over 5 runs.<br>- **Counter:** Internal operations (character comparisons for search; cell iterations/calls for Edit Distance). |
| **Prediction** | 1. **Edit Distance:** Recursive will explode exponentially past $n=15$. Table and Two-Rows will have identical step operations $O(n \cdot m)$, but Two-Rows will save significant memory.<br>2. **String Search:** On random inputs, Naive and KMP will behave similarly linearly. On worst-case repetitive inputs, Naive operations will scale quadratically $O(n \cdot m)$, whereas KMP will remain strictly linear $O(n + m)$. |
| **Plots** | 1. `edit_distance_recursive.png`: Log-scale plot of Recursive calls vs input size $n$.<br>2. `edit_distance_dp_comparison.png`: Comparison of Table vs Two-Rows time & ops on large inputs.<br>3. `string_search_worst_case.png`: Operations count of Naive vs KMP on worst-case repetitive text. |

---

## Plots and Results

### Plot 1: Recursive Edit Distance Operation Growth
![Recursive Edit Distance](results/plots/edit_distance_recursive.png)
*Takeaway:* The operation count for recursive edit distance grows exponentially with input length, rendering computation infeasible beyond $n \approx 16$.

### Plot 2: Table vs Two-Rows DP Scaling
![DP Comparison](results/plots/edit_distance_dp_comparison.png)
*Takeaway:* Both DP approaches perform an identical number of operations $O(n \cdot m)$, but the Two-Rows variant reduces memory footprint significantly with negligible runtime overhead.

### Plot 3: String Search Naive vs KMP (Worst-Case)
![String Search Worst Case](results/plots/string_search_worst_case.png)
*Takeaway:* Under worst-case repetitive patterns, Naive search exhibits quadratic $O(n \cdot m)$ character comparisons, whereas KMP maintains constant linear growth $O(n + m)$.

---

## Prediction vs Result

- **Edit Distance:** Prediction accurately matched empirical observations. Recursive execution suffered exponential slowdown ($O(3^{\min(n,m)})$), whereas DP versions computed $n=2000$ in milliseconds.
- **String Search:** On random inputs, Naive search was remarkably fast due to early mismatches (averaging 1-2 comparisons per window). However, on worst-case inputs (`AAAA...B`), Naive performance degraded quadratically as predicted, while KMP remained unaffected.

---

## Short Answers

1. **Which input made your string search slowest, and why does it do that?**  
   The worst-case repetitive input (`Text = AAAA...A`, `Pattern = AAAA...B`) made Naive search slowest. It forces Naive search to compare almost all $m$ characters of the pattern before failing on the last character, resetting the window by just 1 position and repeating the process $n - m + 1$ times, causing $O(n \cdot m)$ total comparisons.

2. **How large an $n$ could the recursive Edit Distance handle, and what stopped it?**  
   Recursive Edit Distance could effectively handle inputs only up to $n \approx 15-16$ within reasonable time (<1-2 seconds). It was stopped by stack overflow risks and exponential call tree branching ($O(3^n)$), which recomputes overlapping subproblems millions of times without memoization.

3. **What do you gain and what do you lose when you use two rows instead of the full table?**  
   Using two rows reduces extra memory consumption from $O(n \cdot m)$ down to $O(m)$, allowing processing of multi-megabyte strings without memory exhaustion. However, you lose the ability to backtrace the dynamic programming matrix to reconstruct the actual alignment or sequence of edit operations.

4. **If you had to choose one string search for your city platform, which one and why?**  
   I would choose KMP. City search queries and log files frequently contain repetitive prefix patterns (e.g., street names like "Abylai Khan", "Abylai Han"). KMP guarantees linear runtime $O(n + m)$ regardless of input structure, protecting the system against worst-case latency spikes.

---

## One Paragraph on Time
Measured wall-clock times showed clear relative performance trends, but varied across runs due to CPU scheduling, JVM garbage collection, and background OS tasks. Internal operation counters provided deterministic, hardware-independent proof of algorithmic complexity, validating theoretical asymptotic bounds alongside clock measurements.
