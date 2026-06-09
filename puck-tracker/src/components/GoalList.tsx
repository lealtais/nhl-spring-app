import { Goal } from '../types';

interface GoalListProps {
  goals: Goal[];
  selectedGoal: Goal | null;
  onGoalSelect: (goal: Goal) => void;
  teamColor: string;
  accentColor: string;
}

function proxyImageUrl(url: string): string {
  if (!url) return '';
  return `https://images.weserv.nl/?url=${encodeURIComponent(url)}`;
}

export default function GoalList({ goals, selectedGoal, onGoalSelect, teamColor, accentColor }: GoalListProps) {
  return (
    <div className="goal-list-section">
      <div className="goal-list-header">
        <span className="goal-list-title">Match Goals Scored</span>
        <span className="goal-list-count" style={{ color: accentColor }}>
          {goals.length} Goals Total
        </span>
      </div>

      <div className="goal-list">
        {goals.map((goal) => {
          const isSelected = selectedGoal?.eventId === goal.eventId;
          return (
            <div
              key={goal.eventId}
              className={`goal-card ${isSelected ? 'goal-card--selected' : ''}`}
              onClick={() => onGoalSelect(goal)}
              style={{
                backgroundColor: isSelected ? `${teamColor}1F` : undefined,
                borderColor: isSelected ? accentColor : undefined,
              }}
            >
              <div
                className="goal-card-avatar"
                style={{ borderColor: isSelected ? accentColor : 'rgba(156,163,175,0.3)' }}
              >
                {goal.playerPhoto ? (
                  <img
                    src={proxyImageUrl(goal.playerPhoto)}
                    alt={goal.player}
                    onError={(e) => {
                      (e.target as HTMLImageElement).style.display = 'none';
                      (e.target as HTMLImageElement).nextElementSibling?.classList.remove('hidden');
                    }}
                  />
                ) : null}
                <span className={`avatar-fallback ${goal.playerPhoto ? 'hidden' : ''}`} style={{ color: accentColor }}>👤</span>
              </div>

              <div className="goal-card-info">
                <span className="goal-card-player">{goal.player}</span>
                <span className="goal-card-detail">
                  Period {goal.period} - {goal.time} | {goal.shotType}
                </span>
              </div>

              <div className="goal-card-team-badge">
                Team: {goal.team}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
