import numpy as np
import time
from training import train_bot
from cat_env import make_env

def test_single_cat(cat_name, num_trials=10, verbose=True):
    """Test a single cat over multiple trials"""
    steps_list = []
    success_count = 0
    
    for trial in range(num_trials):
        if verbose:
            print(f"  Trial {trial + 1}/{num_trials}...", end=" ")
        
        # Train the bot
        start_time = time.time()
        q_table = train_bot(cat_name, render=-1)
        train_time = time.time() - start_time
        
        # Test the trained bot
        env = make_env(cat_type=cat_name)
        obs, _ = env.reset()
        done = False
        steps = 0
        
        while not done and steps < 60:
            action = int(np.argmax(q_table[obs]))
            obs, reward, terminated, truncated, _ = env.step(action)
            steps += 1
            done = terminated or truncated
        
        env.close()
        
        # Record results
        if terminated:
            steps_list.append(steps)
            success_count += 1
            if verbose:
                print(f"Caught in {steps} steps (trained in {train_time:.1f}s)")
        else:
            steps_list.append(60)
            if verbose:
                print(f"Failed (timeout)")
    
    return {
        'steps': steps_list,
        'avg_steps': np.mean(steps_list),
        'std_steps': np.std(steps_list),
        'min_steps': min(steps_list),
        'max_steps': max(steps_list),
        'success_rate': (success_count / num_trials) * 100,
        'success_count': success_count
    }

def run_full_evaluation(num_trials=10):
    """Run evaluation on all known cats"""
    cats = ['batmeow', 'mittens', 'paotsin', 'squiddyboi', 'peekaboo']
    results = {}
    
    print("="*70)
    print("CatBot Performance Evaluation")
    print("="*70)
    print(f"Configuration: {num_trials} trials per cat, 5000 training episodes each")
    print("="*70)
    
    for cat in cats:
        print(f"\nTesting {cat.upper()}...")
        results[cat] = test_single_cat(cat, num_trials=num_trials)
    
    # Print summary
    print("\n" + "="*70)
    print("RESULTS SUMMARY")
    print("="*70)
    print(f"{'Cat Name':<15} {'Avg Steps':<12} {'Std Dev':<10} {'Success Rate':<15} {'Range':<15}")
    print("-"*70)
    
    for cat, metrics in results.items():
        print(f"{cat.capitalize():<15} "
              f"{metrics['avg_steps']:>6.2f}      "
              f"{metrics['std_steps']:>6.2f}    "
              f"{metrics['success_rate']:>6.1f}%         "
              f"{metrics['min_steps']}-{metrics['max_steps']} steps")
    
    print("="*70)
    
    # Overall statistics
    total_success = sum(r['success_count'] for r in results.values())
    total_trials = num_trials * len(cats)
    overall_success_rate = (total_success / total_trials) * 100
    
    print(f"\nOverall Success Rate: {overall_success_rate:.1f}% ({total_success}/{total_trials} catches)")
    
    # Save to file
    with open('performance_results.txt', 'w') as f:
        f.write("CatBot Performance Results\n")
        f.write("="*70 + "\n\n")
        f.write(f"{'Cat Name':<15} {'Avg Steps':<12} {'Std Dev':<10} {'Success Rate':<15} {'Range':<15}\n")
        f.write("-"*70 + "\n")
        for cat, metrics in results.items():
            f.write(f"{cat.capitalize():<15} "
                   f"{metrics['avg_steps']:>6.2f}      "
                   f"{metrics['std_steps']:>6.2f}    "
                   f"{metrics['success_rate']:>6.1f}%         "
                   f"{metrics['min_steps']}-{metrics['max_steps']} steps\n")
        f.write("\n" + "="*70 + "\n")
        f.write(f"Overall Success Rate: {overall_success_rate:.1f}%\n")
    
    print("\nResults saved to 'performance_results.txt'")
    
    return results

if __name__ == "__main__":
    # Run with 10 trials per cat
    results = run_full_evaluation(num_trials=10)