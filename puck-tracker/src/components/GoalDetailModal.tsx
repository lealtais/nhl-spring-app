import { Goal } from '../types';

interface GoalDetailModalProps {
  goal: Goal;
  accentColor: string;
  onClose: () => void;
}

export default function GoalDetailModal({ goal, accentColor, onClose }: GoalDetailModalProps) {
  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke={accentColor} strokeWidth="2">
            <circle cx="12" cy="12" r="10" />
            <path d="M12 2 L12 22 M2 12 L22 12" />
          </svg>
          <h2>Goal Details!</h2>
        </div>

        <div className="modal-body">
          <p className="modal-scorer">Scorer: {goal.player}</p>
          <div className="modal-details">
            <p>Team: {goal.team}</p>
            <p>Period: {goal.period}</p>
            <p>Time: {goal.time}</p>
            <p>Shot Type: {goal.shotType}</p>
          </div>
          <p className="modal-coords" style={{ color: accentColor }}>
            Rink Coordinates: [X: {goal.x.toFixed(1)}, Y: {goal.y.toFixed(1)}]
          </p>
        </div>

        <div className="modal-actions">
          <button
            className="modal-close-btn"
            onClick={onClose}
            style={{ color: accentColor }}
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
}
